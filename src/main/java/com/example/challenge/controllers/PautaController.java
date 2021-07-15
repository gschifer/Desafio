package com.example.challenge.controllers;

import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.request.PautaRequest;
import com.example.challenge.services.PautaService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/pautas")
public class PautaController {
    private PautaService pautaService;

    @Autowired
    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @ApiOperation("Lista todas as pautas cadastradas.")
    @GetMapping
    public ResponseEntity<List<Pauta>> getPautas() {
        List<Pauta> pautas = pautaService.getPautas();

        return ResponseEntity.ok(pautas);
    }

    @ApiOperation("Lista a pauta desejada pelo ID informado.")
    @GetMapping("/{pautaId}")
    public ResponseEntity<Pauta> getPauta(@PathVariable Long pautaId) {
        Pauta pauta = pautaService.getPauta(pautaId);

        return ResponseEntity.ok(pauta);
    }

    @ApiOperation("Deleta a pauta pelo ID informado.")
    @DeleteMapping("/{pautaId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity deletePauta(@PathVariable Long pautaId) {
        pautaService.deletePauta(pautaId);

        return ResponseEntity.ok().body("Pauta excluída com sucesso.");
    }

    @ApiOperation("Lista todas as pautas que estão com o resultado: 'Empatada'.")
    @GetMapping("/empates")
    public ResponseEntity<List<Pauta>> getPautasEmpatadas() {
        List<Pauta> pautas = pautaService.getPautasEmpatadas();

        return ResponseEntity.ok(pautas);
    }

    @ApiOperation("Lista todas as pautas que estão com o resultado: 'Aprovada'.")
    @GetMapping("/aprovadas")
    public ResponseEntity<List<Pauta>> getPautasAprovadas() {
        List<Pauta> pautas = pautaService.getPautasAprovadas();

        return ResponseEntity.ok(pautas);
    }

    @ApiOperation("Lista todas as pautas que estão com o resultado: 'Reprovada'.")
    @GetMapping("/reprovadas")
    public ResponseEntity<List<Pauta>> getPautasReprovadas() {
        List<Pauta> pautas = pautaService.getPautasReprovadas();

        return ResponseEntity.ok(pautas);
    }

    @ApiOperation("Reabre uma pauta a partir do ID informado.")
    @PostMapping("/{pautaId}/reabrirPauta")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Pauta> reabrePauta(@PathVariable Long pautaId) {
        Pauta pauta = pautaService.reabrePauta(pautaId);

        URI location = this.getUri(pautaId);

        return ResponseEntity.created(location).body(pauta);
    }

    @ApiOperation("Coloca uma pauta em votação por um tempo específico determinado. Obs: Por padrão está como 1 min " +
            "caso não houver um tempo específico determinado por parâmetro.")
    @PostMapping("/{pautaId}/abrirVotacao")
    @Secured({"ROLE_ADMIN"})
    public void colocaPautaEmVotacao(@PathVariable Long pautaId,
                                     @RequestParam(name = "tempo", required = false) Integer tempo) {
        pautaService.colocaPautaEmVotacao(pautaId, tempo);
    }

    @ApiOperation("Cadastra uma nova pauta.")
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Pauta> salvaPauta(@RequestBody PautaRequest pauta) {
        Pauta pautaAux = pautaService.savePauta(pauta);

        URI location = getUri(pautaAux.getId());

        return ResponseEntity.created(location).body(pautaAux);
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
