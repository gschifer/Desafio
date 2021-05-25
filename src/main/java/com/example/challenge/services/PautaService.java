package com.example.challenge.services;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.repository.PautaRepository;
import com.example.challenge.repository.VotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PautaService {
    PautaRepository pautaRepository;
    VotoRepository votoRepository;
    AssociadoService associadoService;

    public boolean validaPauta(Long pautaId) {
        List<Associado> associados = associadoService.getAssociados();
        Optional<Pauta> pauta      = pautaRepository.findById(pautaId);
        List<Voto> votos           = votoRepository.findAll();

        if (pauta.isPresent()) {
            if (pauta.get().getStatus().equals("Aberta")) {
                if (associados.stream().count() > votos.stream().count()) {
                    return true;
                }
                this.updateResultado(pautaId);
            }
        }
        return false;
    }

    private void updateResultado(Long pautaId) {
        List<Voto> voto          = votoRepository.findByPautaId(pautaId);
        Optional<Pauta> pauta    = pautaRepository.findById(pautaId);
        long votosAfavor         = voto.stream().filter(v -> v.getVoto().equals("Sim")).count();
        long votosContra         = voto.stream().filter(v -> v.getVoto().equals("Não")).count();

        if (votosAfavor == votosContra) {
            pauta.get().setResultado("Empate");
        }
        else if (votosAfavor > votosContra) {
            pauta.get().setResultado("Aprovada");
        }
        else {
            pauta.get().setResultado("Reprovada");
        }
        pautaRepository.save(pauta.get());
    }

    @Transactional
    public Pauta savePauta(Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    public List<Pauta> getPautas() {
        List<Pauta> pautas = pautaRepository.findAll();

        return pautas;
    }
}
