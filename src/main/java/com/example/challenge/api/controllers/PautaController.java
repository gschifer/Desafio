package com.example.challenge.api.controllers;

import com.example.challenge.api.assembler.PautaDtoAssembler;
import com.example.challenge.api.dto.PautaDTO;
import com.example.challenge.api.openapi.controller.PautaControllerOpenApi;
import com.example.challenge.api.request.PautaRequest;
import com.example.challenge.domain.services.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/pautas", produces = MediaType.APPLICATION_JSON_VALUE)
public class PautaController implements PautaControllerOpenApi {
    private PautaService pautaService;
    private PautaDtoAssembler pautaDtoAssembler;

    @Autowired
    public PautaController(PautaService pautaService, PautaDtoAssembler pautaDtoAssembler) {
        this.pautaService = pautaService;
        this.pautaDtoAssembler = pautaDtoAssembler;
    }

    @GetMapping
    public ResponseEntity<List<PautaDTO>> getPautas() {
        List<PautaDTO> pautas = pautaDtoAssembler.toCollectionOfDto(pautaService.getPautas());

        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/{pautaId}")
    public ResponseEntity<PautaDTO> getPauta(@PathVariable Long pautaId) {
        PautaDTO pauta = pautaDtoAssembler.pautaToDto(pautaService.getPauta(pautaId));

        return ResponseEntity.ok(pauta);
    }

    @DeleteMapping("/{pautaId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity deletePauta(@PathVariable Long pautaId) {
        pautaService.deletePauta(pautaId);

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/empates")
    public ResponseEntity<List<PautaDTO>> getPautasEmpatadas() {
        List<PautaDTO> pautas = pautaDtoAssembler.toCollectionOfDto(pautaService.getPautasEmpatadas());

        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/aprovadas")
    public ResponseEntity<List<PautaDTO>> getPautasAprovadas() {
        List<PautaDTO> pautas = pautaDtoAssembler.toCollectionOfDto(pautaService.getPautasAprovadas());

        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/reprovadas")
    public ResponseEntity<List<PautaDTO>> getPautasReprovadas() {
        List<PautaDTO> pautas = pautaDtoAssembler.toCollectionOfDto(pautaService.getPautasReprovadas());

        return ResponseEntity.ok(pautas);
    }

    @PostMapping("/{pautaId}/reabrirPauta")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<PautaDTO> reabrePauta(@PathVariable Long pautaId) {
        PautaDTO pauta = pautaDtoAssembler.pautaToDto(pautaService.reabrePauta(pautaId));

        URI location = this.getUri(pautaId);

        return ResponseEntity.created(location).body(pauta);
    }

    @PostMapping("/{pautaId}/abrirVotacao")
    @Secured({"ROLE_ADMIN"})
    public void colocaPautaEmVotacao(@PathVariable Long pautaId,
                                     @RequestParam(name = "tempo", required = false) Integer tempo) {
        pautaService.colocaPautaEmVotacao(pautaId, tempo);
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<PautaDTO> salvaPauta(@RequestBody PautaRequest pautaRequest) {
        PautaDTO pauta = pautaDtoAssembler.pautaToDto(pautaService.savePauta(pautaRequest));
        URI location = getUri(pauta.getId());

        return ResponseEntity.created(location).body(pauta);
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
