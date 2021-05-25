package com.example.challenge.controllers;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.services.AssociadoService;
import com.example.challenge.services.PautaService;
import com.example.challenge.services.VotoService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class PautaController {
    PautaService pautaService;
    AssociadoService associadoService;
    VotoService votoService;


    @GetMapping("/pautas")
    public ResponseEntity getPautas() {
        List<Pauta> pautas = pautaService.getPautas();

        return pautas.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(pautas);
    }

    @PostMapping("/pautas")
    public ResponseEntity salvaPauta(@RequestBody Pauta pauta) {
        Pauta pautaAux = pautaService.savePauta(pauta);

        URI location = getUri(pautaAux.getId());

        return ResponseEntity.created(location).body(pautaAux);

    }

    @PostMapping("/pauta/{pautaId}/associado/{associadoId}")
    public ResponseEntity salvaVotacao(@RequestBody Voto voto,
                                       @PathVariable Long pautaId,
                                       @PathVariable Long associadoId) {

        boolean validaAssociado        = associadoService.validaAssociado(associadoId);
        boolean pauta                  = pautaService.validaPauta(pautaId);
        boolean votoVerificado         = votoService.validaVoto(voto);

        if (validaAssociado && pauta && votoVerificado) {
            Voto votoSalvo = this.saveVoto(associadoId, pautaId, voto);
            this.updateAssociado(associadoId, pautaId, votoSalvo);
            pautaService.validaPauta(pautaId);
            URI location = getUri(votoSalvo.getId());

            return ResponseEntity.created(location).body(votoSalvo);
        }

        return ResponseEntity.notFound().build();
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }

    private void updateAssociado(Long associadoId, Long pautaId, Voto votoSalvo ) {
        Optional<Associado> associado = associadoService.getAssociado(associadoId);
        associado.get().setPautaId(pautaId);
        associado.get().setVotoId(votoSalvo.getId());
        Associado assoc = associado.get();
        associadoService.updateAssociado(associadoId, assoc);
    }

    private Voto saveVoto(Long associadoId, Long pautaId, Voto voto) {
        voto.setAssociadoId(associadoId);
        voto.setPautaId(pautaId);

        return votoService.saveVoto(voto);
    }
}
