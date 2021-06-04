package com.example.challenge.services;

import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.exceptions.EmptyListException;
import com.example.challenge.exceptions.ObjectNotFoundException;
import com.example.challenge.repository.PautaRepository;
import com.example.challenge.repository.VotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class PautaService {
    PautaRepository pautaRepository;
    VotoRepository votoRepository;
    AssociadoService associadoService;

    @Autowired
    public PautaService(PautaRepository pautaRepository, VotoRepository votoRepository, AssociadoService associadoService) {
        this.pautaRepository = pautaRepository;
        this.votoRepository = votoRepository;
        this.associadoService = associadoService;
    }

    public Optional<Pauta> getPauta(Long pautaId) {
        Optional<Pauta> pauta = pautaRepository.findById(pautaId);

        if (pauta.isEmpty()) {
            throw new ObjectNotFoundException("A pauta de ID: " + pautaId + " não existe na base de dados.");
        }

        return pauta;
    }

//    public boolean validaPauta(Long pautaId) {
//        List<Associado> associados = associadoService.getAssociados();
//        Optional<Pauta> pauta      = pautaRepository.findById(pautaId);
//        List<Voto> votos           = votoRepository.findAll();
//
//        if (pauta.isPresent()) {
//            if (pauta.get().getStatus().equals("Aberta")) {
//                if (associados.stream().count() > votos.stream().count()) {
//                    return true;
//                }
//                this.updateResultado(pautaId);
//            }
//        }
//        return false;
//    }
//
//    public void updateResultado(Long pautaId) {
//        List<Voto> voto          = votoRepository.findByPautaId(pautaId);
//        Optional<Pauta> pauta    = pautaRepository.findById(pautaId);
//        long votosAfavor         = voto.stream().filter(v -> v.getVoto().equals("Sim")).count();
//        long votosContra         = voto.stream().filter(v -> v.getVoto().equals("Não")).count();
//
//        if ((votosAfavor == votosContra) || (votosAfavor < votosContra)) {
//            pauta.get().setResultado("Reprovada");
//        } else {
//            pauta.get().setResultado("Aprovada");
//        }
//
//        pauta.get().setStatus("Encerrada");
//        pautaRepository.save(pauta.get());
//    }

    @Transactional
    public Pauta savePauta(Pauta pauta) {
        Assert.isNull(pauta.getId());
        return pautaRepository.save(pauta);
    }

    public List<Pauta> getPautas() {
        List<Pauta> pautas = pautaRepository.findAll();

        if (pautas.isEmpty()) {
            throw new EmptyListException("Não há nenhuma pauta cadastrada no momento");
        }

        return pautas;
    }
}
