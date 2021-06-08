package com.example.challenge.services;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.exceptions.EmptyListException;
import com.example.challenge.exceptions.ObjectNotFoundException;
import com.example.challenge.exceptions.VotoInvalidoException;
import com.example.challenge.repository.AssociadoRepository;
import com.example.challenge.repository.VotoRepository;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class AssociadoService {
    private AssociadoRepository associadoRepository;
    private VotoRepository votoRepository;

    @Autowired
    public AssociadoService(AssociadoRepository associadoRepository, VotoRepository votoRepository) {
        this.associadoRepository = associadoRepository;
        this.votoRepository = votoRepository;
    }

    public Optional<Associado> getAssociado(Long associadoId) {
        Optional<Associado> associado = associadoRepository.findById(associadoId);

        return Optional.ofNullable(associado.orElseThrow(() -> new ObjectNotFoundException
                ("O associado de ID: " + associadoId +" não existe na base de dados.")));
    }

    public void validaAssociado(Long associadoId, Long pautaId) {
        Optional<Voto> voto = votoRepository.findByAssociadoIdAndPautaId(associadoId, pautaId);

        if (voto.isPresent()) throw new VotoInvalidoException("Associado já votou nesta pauta.");
    }


    @Transactional
    public Associado saveAssociado(Associado associado) {
        Assert.isNull(associado.getId());
        return associadoRepository.save(associado);
    }

    public List<Associado> getAssociados() {
        List<Associado> associados = associadoRepository.findAll();

        if (associados.isEmpty()) {
            throw new EmptyListException("Não há associados cadastrados no momento.");
        }

        return associados;
    }

    @Transactional
    public void deleteAssociado(Long associadoId) {
        associadoRepository.deleteById(associadoId);
    }

    @Transactional
    public Associado updateAssociado(Long associadoId, Associado associadoRequest) {
        Optional<Associado> associado = this.getAssociado(associadoId);
        assertNull(associadoRequest.getId());

            Associado assoc = associado.get();
            assoc.setEmail(associadoRequest.getEmail());
            assoc.setNome(associadoRequest.getNome());
            assoc.setCpf(associadoRequest.getCpf());

            return associadoRepository.save(assoc);

    }
}
