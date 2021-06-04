package com.example.challenge.controllers;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.exceptions.ExceptionError;
import com.example.challenge.services.AssociadoService;
import com.example.challenge.services.PautaService;
import com.example.challenge.services.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public ResponseEntity<List<Associado>> getAssociados() {
        List<Associado> associados = associadoService.getAssociados();

        return ResponseEntity.ok(associados);
    }

    @PostMapping
    public ResponseEntity<Associado> salvaAssociado(@RequestBody @Valid Associado associado) {
        Associado associadoAux = associadoService.saveAssociado(associado);
        URI location = getUri(associadoAux.getId());

        return ResponseEntity.created(location).body(associadoAux);
    }

    @GetMapping("/{associadoId}")
    public ResponseEntity<Optional<Associado>> getAssociado(@PathVariable Long associadoId) {
        Optional<Associado> associado = associadoService.getAssociado(associadoId);

        return ResponseEntity.ok(associado);
    }

    @PostMapping("/{associadoId}/votar/{pautaId}")
    public Voto votaEmUmaPauta(@RequestBody Voto voto,
                               @PathVariable Long associadoId,
                               @PathVariable Long pautaId) {

        Optional<Associado> associado = associadoService.getAssociado(associadoId);
        voto.setAssociado(associado.get());
        voto.setPauta(pautaService.getPauta(pautaId).get());
        Voto votoSalvo = votoService.saveVoto(voto);

        return votoSalvo;

    }

    @PostMapping("/verificaCpf")
    public ResponseEntity<Object> verificaCpf(@RequestBody String cpf) {
        RestTemplate restTemplate = new RestTemplate();
        Object response = restTemplate.getForObject("https://user-info.herokuapp.com/users/" + cpf, String.class);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{associadoId}")
    public ResponseEntity deleteAssociado(@PathVariable Long associadoId) {
        associadoService.deleteAssociado(associadoId);

        return ResponseEntity.ok().build();
    }

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
