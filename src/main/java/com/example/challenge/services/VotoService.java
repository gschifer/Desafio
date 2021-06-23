package com.example.challenge.services;

import com.example.challenge.domain.entities.Voto;
import com.example.challenge.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService {
    private VotoRepository votoRepository;
    private PautaService pautaService;

    @Autowired
    public VotoService(VotoRepository votoRepository, PautaService pautaService) {
        this.votoRepository = votoRepository;
        this.pautaService = pautaService;
    }

    public Voto saveVoto(Voto voto) {
        Voto votoSalvo = votoRepository.save(voto);

        return votoSalvo;
    }
}
