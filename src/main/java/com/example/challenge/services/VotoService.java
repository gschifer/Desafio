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
}
