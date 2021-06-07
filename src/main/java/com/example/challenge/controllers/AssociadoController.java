package com.example.challenge.controllers;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.services.AssociadoService;
import com.example.challenge.services.PautaService;
import com.example.challenge.services.VotoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/associados")
public class AssociadoController {
    private AssociadoService associadoService;
    private VotoService votoService;
    private PautaService pautaService;

    @Autowired
    public AssociadoController(AssociadoService associadoService, VotoService votoService, PautaService pautaService) {
        this.associadoService = associadoService;
        this.votoService = votoService;
        this.pautaService = pautaService;
    }

    @ApiOperation("Lista todos os associados cadastrados.")
    @GetMapping
    public ResponseEntity<List<Associado>> getAssociados() {
        List<Associado> associados = associadoService.getAssociados();

        return ResponseEntity.ok(associados);
    }

    @ApiOperation("Cadastra um novo associado.")
    @PostMapping
    public ResponseEntity<Associado> salvaAssociado(@RequestBody @Valid Associado associado) {
        Associado associadoAux = associadoService.saveAssociado(associado);
        URI location = getUri(associadoAux.getId());

        return ResponseEntity.created(location).body(associadoAux);
    }

    @ApiOperation("Lista o associado desejado pelo ID informado.")
    @GetMapping("/{associadoId}")
    public ResponseEntity<Optional<Associado>> getAssociado(@PathVariable Long associadoId) {
        Optional<Associado> associado = associadoService.getAssociado(associadoId);

        return ResponseEntity.ok(associado);
    }

    @ApiOperation("Cadastra o voto do associado para a pauta especificada.")
    @PostMapping("/{associadoId}/votar/{pautaId}")
    public Voto votaEmUmaPauta(@RequestBody Voto voto,
                               @PathVariable Long associadoId,
                               @PathVariable Long pautaId) {

        associadoService.validaAssociado(associadoId, pautaId);
        pautaService.validaPauta(pautaId);

        Optional<Associado> associado = associadoService.getAssociado(associadoId);
        voto.setAssociado(associado.get());
        voto.setPauta(pautaService.getPauta(pautaId).get());
        Voto votoSalvo = votoService.saveVoto(voto);

        return votoSalvo;
    }

    @ApiOperation("Verifica se o CPF informado é válido.")
    @PostMapping("/verificaCpf")
    public ResponseEntity<Object> verificaCpf(@RequestBody String cpf) {
        RestTemplate restTemplate = new RestTemplate();
        Object response = restTemplate.getForObject("https://user-info.herokuapp.com/users/" + cpf, String.class);

        return ResponseEntity.ok(response);
    }

    @ApiOperation("Deleta um associado pelo ID informado.")
    @DeleteMapping("/{associadoId}")
    public ResponseEntity<Associado> deleteAssociado(@PathVariable Long associadoId) {
        associadoService.deleteAssociado(associadoId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("Atualiza os dados do associado pelo ID informado.")
    @PutMapping("/{associadoId}")
    public ResponseEntity<Associado> updateAssociado(@PathVariable Long associadoId,
                                                     @RequestBody Associado associado) {

        Associado associadoReturn = associadoService.updateAssociado(associadoId, associado);

        return ResponseEntity.ok(associadoReturn);
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
