package com.example.challenge.services;

import com.example.challenge.domain.entities.Voto;
import com.example.challenge.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class VotoService {
    @Autowired
    VotoRepository repository;


    public Voto saveVoto(Voto voto) {
       return repository.save(voto);
    }

    public boolean validaVoto(Voto voto) {
        Optional<Voto> validaVoto = repository.findByAssociadoId(voto.getAssociadoId());

        if (validaVoto.isPresent()) {
            return false;
        }

        if (voto.getVoto().equals("Sim") || voto.getVoto().equals("Não")) {
            return true;
        }

        return false;
    }


}
