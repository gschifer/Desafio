package com.example.challenge.services;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.exceptions.ObjectNotFoundException;
import com.example.challenge.repository.AssociadoRepository;
import com.example.challenge.repository.VotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AssociadoService {
    AssociadoRepository associadoRepository;
    VotoRepository votoRepository;

    public Optional<Associado> getAssociado(Long associadoId) {
        Optional<Associado> associado = associadoRepository.findById(associadoId);

        return Optional.ofNullable(associado.orElseThrow(() -> new ObjectNotFoundException
                ("O associado de ID: " + associadoId +" não existe na base de dados.")));
    }

    public boolean validaAssociado(Long associadoId) {
        Optional<Associado> associado = this.getAssociado(associadoId);

            if (associado.isPresent()) {
                return votoRepository.findByAssociadoId(associadoId).isEmpty();
            }

        return false;
    }

    @Transactional
    public Associado saveAssociado(Associado associado) {
        return associadoRepository.save(associado);
    }

    public List<Associado> getAssociados() {
        List<Associado> associados = associadoRepository.findAll();

        return associados;
    }

    @Transactional
    public void deleteAssociado(Long associadoId) {
        associadoRepository.deleteById(associadoId);
    }

    @Transactional
    public Associado updateAssociado(Long associadoId, Associado associadoRequest) {
        Optional<Associado> associado = this.getAssociado(associadoId);

            Associado assoc = associado.get();
            assoc.setEmail(associadoRequest.getEmail());
            assoc.setNome(associadoRequest.getNome());

           return this.saveAssociado(assoc);
    }
}
