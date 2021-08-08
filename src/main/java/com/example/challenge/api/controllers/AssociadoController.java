package com.example.challenge.api.controllers;

import com.example.challenge.api.assembler.AssociadoDtoAssembler;
import com.example.challenge.api.dto.AssociadoDTO;
import com.example.challenge.api.openapi.controller.AssociadoControllerOpenApi;
import com.example.challenge.api.request.AssociadoRequest;
import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.domain.services.AssociadoService;
import com.example.challenge.domain.services.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "api/v1/associados", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssociadoController implements AssociadoControllerOpenApi {

    private AssociadoService associadoService;
    private PautaService pautaService;
    private AssociadoDtoAssembler associadoDtoAssembler;

    @Autowired
    public AssociadoController(AssociadoService associadoService, PautaService pautaService, AssociadoDtoAssembler associadoDtoAssembler) {
        this.associadoService = associadoService;
        this.pautaService = pautaService;
        this.associadoDtoAssembler = associadoDtoAssembler;
    }

    @GetMapping
    public ResponseEntity<List<AssociadoDTO>> getAssociados() {
        List<AssociadoDTO> associados = associadoDtoAssembler.toCollectionOfDto(associadoService.getAssociados());

        return ResponseEntity.ok(associados);
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<AssociadoDTO> salvaAssociado(@RequestBody @Valid AssociadoRequest associadoRequest) {
        AssociadoDTO associado = associadoDtoAssembler.associadoToDto(associadoService.saveAssociado(associadoRequest));
        URI location = getUri(associado.getId());

        return ResponseEntity.created(location).body(associado);
    }

    @GetMapping("/{associadoId}")
    public ResponseEntity<AssociadoDTO> getAssociado(@PathVariable Long associadoId) {
        AssociadoDTO associado = associadoDtoAssembler.associadoToDto(associadoService.getAssociado(associadoId));
        return ResponseEntity.ok(associado);
    }

    @PostMapping("/{associadoId}/votar/{pautaId}")
    @Secured({"ROLE_ADMIN"})
    public Voto votaEmUmaPauta(@RequestBody Voto voto,
                               @PathVariable Long associadoId,
                               @PathVariable Long pautaId) {
        return pautaService.votaNaPauta(voto, associadoId, pautaId);
    }

    @DeleteMapping("/{associadoId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Associado> deleteAssociado(@PathVariable Long associadoId) {
        associadoService.deleteAssociado(associadoId);

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{associadoId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<AssociadoDTO> updateAssociado(@PathVariable Long associadoId,
                                                        @RequestBody Associado associadoToUpdate) {

        AssociadoDTO associado = associadoDtoAssembler.associadoToDto
                (associadoService.updateAssociado(associadoId, associadoToUpdate));

        return ResponseEntity.ok(associado);
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
