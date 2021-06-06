package com.example.challenge.controllers;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.services.AssociadoService;
import com.example.challenge.services.PautaService;
import com.example.challenge.services.VotoService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/pautas")
public class PautaController {
    private PautaService pautaService;
    private AssociadoService associadoService;
    private VotoService votoService;

    @Autowired
    public PautaController(PautaService pautaService, AssociadoService associadoService, VotoService votoService) {
        this.pautaService = pautaService;
        this.associadoService = associadoService;
        this.votoService = votoService;
    }

    @GetMapping
    public ResponseEntity<List<Pauta>> getPautas() {
        List<Pauta> pautas = pautaService.getPautas();

        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/{pautaId}")
    public ResponseEntity<Optional<Pauta>> getPauta(@PathVariable Long pautaId) {
        Optional<Pauta> pauta = pautaService.getPauta(pautaId);

        return ResponseEntity.ok(pauta);
    }

    @GetMapping("/empates")
    public ResponseEntity<List<Pauta>> getPautasEmpatadas() {
        List<Pauta> pautas = pautaService.getPautasEmpatadas();

        return ResponseEntity.ok(pautas);
    }

    @PostMapping("/{pautaId}/reabrirPauta")
    public ResponseEntity<Pauta> reabrePauta(@PathVariable Long pautaId) {
       Pauta pauta = pautaService.reabrePauta(pautaId);

        URI location = this.getUri(pautaId);

        return ResponseEntity.created(location).body(pauta);
    }

    @PostMapping
    public ResponseEntity<Pauta> salvaPauta(@RequestBody Pauta pauta) {
        Pauta pautaAux = pautaService.savePauta(pauta);

        URI location = getUri(pautaAux.getId());

        return ResponseEntity.created(location).body(pautaAux);
    }


    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
