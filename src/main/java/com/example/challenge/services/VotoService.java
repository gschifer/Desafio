package com.example.challenge.services;

import com.example.challenge.domain.entities.Voto;
import com.example.challenge.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService {
    private VotoRepository votoRepository;

    @Autowired
    public VotoService(VotoRepository votoRepository) {
        this.votoRepository = votoRepository;
    }

    public Voto saveVoto(Voto voto) {
        return votoRepository.save(voto);
    }
}
