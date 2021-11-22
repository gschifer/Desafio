package com.example.challenge.domain.services;

import com.example.challenge.domain.entities.Voto;
import com.example.challenge.domain.exceptions.pautaExceptions.PautaSemVotosException;
import com.example.challenge.domain.exceptions.votoExceptions.VotoNaoEncontradoException;
import com.example.challenge.domain.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VotoService {
    private VotoRepository votoRepository;

    @Autowired
    public VotoService(VotoRepository votoRepository) {
        this.votoRepository = votoRepository;
    }

    @Transactional
    public Voto saveVoto(Voto voto) {
        return votoRepository.save(voto);
    }

    public List<Voto> getVotosByPautaId(Long pautaId) {
        return votoRepository.findByPautaId(pautaId).orElseThrow(PautaSemVotosException::new);
    }

    @Transactional
    public void deleteById(Long votoId) {
        try {
            votoRepository.deleteById(votoId);
        } catch (EmptyResultDataAccessException ex) {
            throw new VotoNaoEncontradoException(votoId);
        }
    }
}
