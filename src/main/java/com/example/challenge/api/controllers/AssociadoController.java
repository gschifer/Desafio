package com.example.challenge.api.controllers;

import com.example.challenge.api.assembler.AssociadoDtoAssembler;
import com.example.challenge.api.assembler.VotoDtoAssembler;
import com.example.challenge.api.dto.AssociadoDTO;
import com.example.challenge.api.dto.VotoDTO;
import com.example.challenge.api.model.view.AssociadoView;
import com.example.challenge.api.model.view.VotoView;
import com.example.challenge.api.openapi.controller.AssociadoControllerOpenApi;
import com.example.challenge.api.request.AssociadoRequest;
import com.example.challenge.api.request.VotoRequest;
import com.example.challenge.domain.services.AssociadoService;
import com.example.challenge.domain.services.PautaService;
import com.fasterxml.jackson.annotation.JsonView;
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
    private VotoDtoAssembler votoDtoAssembler;

    @Autowired
    public AssociadoController(AssociadoService associadoService, PautaService pautaService,
                               AssociadoDtoAssembler associadoDtoAssembler, VotoDtoAssembler votoDtoAssembler) {
        this.associadoService = associadoService;
        this.pautaService = pautaService;
        this.associadoDtoAssembler = associadoDtoAssembler;
        this.votoDtoAssembler = votoDtoAssembler;
    }

    @JsonView(AssociadoView.Brief.class)
    @GetMapping
    public ResponseEntity<List<AssociadoDTO>> getAssociados() {
        List<AssociadoDTO> associados = associadoDtoAssembler.toCollectionOfDto(associadoService.getAssociados());

        return ResponseEntity.ok(associados);
    }

    @JsonView(AssociadoView.Brief.class)
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<AssociadoDTO> salvaAssociado(@RequestBody @Valid AssociadoRequest associadoRequest) {
        AssociadoDTO associado = associadoDtoAssembler.associadoToDto(associadoService.saveAssociado(associadoRequest));
        URI location = getUri(associado.getId());

        return ResponseEntity.created(location).body(associado);
    }

    @JsonView(AssociadoView.Brief.class)
    @GetMapping("/{associadoId}")
    public ResponseEntity<AssociadoDTO> getAssociado(@PathVariable Long associadoId) {
        AssociadoDTO associado = associadoDtoAssembler.associadoToDto(associadoService.getAssociado(associadoId));

        return ResponseEntity.ok(associado);
    }

    @JsonView(VotoView.Brief.class)
    @PostMapping("/{associadoId}/votar/{pautaId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<VotoDTO> votaEmUmaPauta(@RequestBody @Valid VotoRequest votoRequest,
                                                  @PathVariable Long associadoId,
                                                  @PathVariable Long pautaId) {

        VotoDTO voto = votoDtoAssembler.votoToDto(pautaService.votaNaPauta(votoRequest, associadoId, pautaId));

        return ResponseEntity.ok(voto);
    }

    @DeleteMapping("/{associadoId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> deleteAssociado(@PathVariable Long associadoId) {
        associadoService.deleteAssociado(associadoId);

        return ResponseEntity.notFound().build();
    }

    @JsonView(AssociadoView.Brief.class)
    @PutMapping("/{associadoId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<AssociadoDTO> updateAssociado(@PathVariable Long associadoId,
                                                        @RequestBody @Valid AssociadoRequest associadoRequest) {
        return ResponseEntity.ok(associadoDtoAssembler.associadoToDto(
                associadoService.updateAssociado(associadoId, associadoRequest)));
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
