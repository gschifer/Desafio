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
    PautaService pautaService;
    AssociadoService associadoService;
    VotoService votoService;

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

    @GetMapping
    public ResponseEntity<Optional<Pauta>> getPauta(@PathVariable Long id) {
        Optional<Pauta> pauta = pautaService.getPauta(id);

        return ResponseEntity.ok(pauta);
    }

    @PostMapping
    public ResponseEntity<Pauta> salvaPauta(@RequestBody Pauta pauta) {
        Pauta pautaAux = pautaService.savePauta(pauta);

        URI location = getUri(pautaAux.getId());

        return ResponseEntity.created(location).body(pautaAux);
    }

//    @PostMapping("/{pautaId}/associado/{associadoId}")
//    public ResponseEntity salvaVotacao(@RequestBody Voto voto,
//                                       @PathVariable Long pautaId,
//                                       @PathVariable Long associadoId) {
//
//        boolean validaAssociado        = associadoService.validaAssociado(associadoId);
//        boolean pauta                  = pautaService.validaPauta(pautaId);
//
//        if (validaAssociado && pauta) {
//            Voto votoSalvo = this.saveVoto(associadoId, pautaId, voto);
//            this.updateAssociado(associadoId, pautaId, votoSalvo);
//            pautaService.validaPauta(pautaId);
//            URI location = getUri(votoSalvo.getId());
//
//            return ResponseEntity.created(location).body(votoSalvo);
//        }
//
//        return ResponseEntity.notFound().build();
//    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }

//    private void updateAssociado(Long associadoId, Long pautaId, Voto votoSalvo ) {
//        Optional<Associado> associado = associadoService.getAssociado(associadoId);
//        associado.get().setPautaId(pautaId);
//        associado.get().setVotoId(votoSalvo.getId());
//        Associado assoc = associado.get();
//        associadoService.updateAssociado(associadoId, assoc);
//    }
//
//    private Voto saveVoto(Long associadoId, Long pautaId, Voto voto) {
//        voto.setAssociadoId(associadoId);
//        voto.setPautaId(pautaId);
//
//        return votoService.saveVoto(voto);
//    }
}
