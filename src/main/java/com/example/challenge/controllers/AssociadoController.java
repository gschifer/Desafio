package com.example.challenge.controllers;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.domain.request.AssociadoRequest;
import com.example.challenge.services.AssociadoService;
import com.example.challenge.services.PautaService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/associados")
public class AssociadoController {
    private AssociadoService associadoService;
    private PautaService pautaService;

    @Autowired
    public AssociadoController(AssociadoService associadoService, PautaService pautaService) {
        this.associadoService = associadoService;
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
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Associado> salvaAssociado(@RequestBody @Valid AssociadoRequest associado) {
        Associado associadoAux = associadoService.saveAssociado(associado);
        URI location = getUri(associadoAux.getId());

        return ResponseEntity.created(location).body(associadoAux);
    }

    @ApiOperation("Lista o associado desejado pelo ID informado.")
    @GetMapping("/{associadoId}")
    public ResponseEntity<Associado> getAssociado(@PathVariable Long associadoId) {
        Associado associado = associadoService.getAssociado(associadoId);

        return ResponseEntity.ok(associado);
    }

    @ApiOperation("Cadastra o voto do associado para a pauta especificada.")
    @PostMapping("/{associadoId}/votar/{pautaId}")
    @Secured({"ROLE_ADMIN"})
    public Voto votaEmUmaPauta(@RequestBody Voto voto, @PathVariable Long associadoId, @PathVariable Long pautaId) {
        return pautaService.votaNaPauta(voto, associadoId, pautaId);
    }

    @ApiOperation("Deleta um associado pelo ID informado.")
    @DeleteMapping("/{associadoId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Associado> deleteAssociado(@PathVariable Long associadoId) {
        associadoService.deleteAssociado(associadoId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("Atualiza os dados do associado pelo ID informado.")
    @PutMapping("/{associadoId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Associado> updateAssociado(@PathVariable Long associadoId,
                                                     @RequestBody AssociadoRequest request) {

        return ResponseEntity.ok(associadoService.updateAssociado(associadoId, request));
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
