package com.example.challenge.api.controllers;

import com.example.challenge.api.openapi.controller.PautaControllerOpenApi;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.request.PautaRequest;
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

    @Autowired
    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @GetMapping
    public ResponseEntity<List<Pauta>> getPautas() {
        List<Pauta> pautas = pautaService.getPautas();

        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/{pautaId}")
    public ResponseEntity<Pauta> getPauta(@PathVariable Long pautaId) {
        Pauta pauta = pautaService.getPauta(pautaId);

        return ResponseEntity.ok(pauta);
    }

    @DeleteMapping("/{pautaId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity deletePauta(@PathVariable Long pautaId) {
        pautaService.deletePauta(pautaId);

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/empates")
    public ResponseEntity<List<Pauta>> getPautasEmpatadas() {
        List<Pauta> pautas = pautaService.getPautasEmpatadas();

        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/aprovadas")
    public ResponseEntity<List<Pauta>> getPautasAprovadas() {
        List<Pauta> pautas = pautaService.getPautasAprovadas();

        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/reprovadas")
    public ResponseEntity<List<Pauta>> getPautasReprovadas() {
        List<Pauta> pautas = pautaService.getPautasReprovadas();

        return ResponseEntity.ok(pautas);
    }

    @PostMapping("/{pautaId}/reabrirPauta")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Pauta> reabrePauta(@PathVariable Long pautaId) {
        Pauta pauta = pautaService.reabrePauta(pautaId);

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
    public ResponseEntity<Pauta> salvaPauta(@RequestBody PautaRequest pautaRequest) {
        Pauta pauta = pautaService.savePauta(pautaRequest);
        URI location = getUri(pauta.getId());

        return ResponseEntity.created(location).body(pauta);
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
