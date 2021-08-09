package com.example.challenge.domain.services;

import com.example.challenge.api.assembler.AssociadoDtoAssembler;
import com.example.challenge.api.mapper.AssociadoMapper;
import com.example.challenge.api.request.AssociadoRequest;
import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.domain.exceptions.EmptyListException;
import com.example.challenge.domain.exceptions.associadoExceptions.AssociadoNaoEncontradoException;
import com.example.challenge.domain.exceptions.votoExceptions.VotoInvalidoException;
import com.example.challenge.domain.repository.AssociadoRepository;
import com.example.challenge.domain.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AssociadoService {
    private AssociadoRepository associadoRepository;
    private VotoRepository votoRepository;
    private AssociadoMapper associadoMapper;
    private AssociadoDtoAssembler associadoDtoAssembler;

    @Autowired
    public AssociadoService(AssociadoRepository associadoRepository, VotoRepository votoRepository, AssociadoMapper associadoMapper, AssociadoDtoAssembler associadoDtoAssembler) {
        this.associadoRepository = associadoRepository;
        this.votoRepository = votoRepository;
        this.associadoMapper = associadoMapper;
        this.associadoDtoAssembler = associadoDtoAssembler;
    }

    public Associado getAssociado(Long associadoId) {
        return associadoRepository.findById(associadoId).orElseThrow(() -> new AssociadoNaoEncontradoException(associadoId));
    }

    public void validaAssociado(Long associadoId, Long pautaId) {
        Optional<Voto> voto = votoRepository.findByAssociadoIdAndPautaId(associadoId, pautaId);

        if (voto.isPresent()) {
            throw new VotoInvalidoException("Associado já votou nesta pauta.");
        }
    }

    @Transactional
    public Associado saveAssociado(AssociadoRequest request) {
        return associadoRepository.save(associadoMapper.map(request));
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
        try {
            associadoRepository.deleteById(associadoId);
        } catch (EmptyResultDataAccessException ex) {
            throw new AssociadoNaoEncontradoException(associadoId);
        }
    }

    @Transactional
    public Associado updateAssociado(Long associadoId, AssociadoRequest associadoRequest) {
        Associado associadoAtual = getAssociado(associadoId);
        associadoDtoAssembler.copyProperties(associadoRequest, associadoAtual);

        return associadoRepository.save(associadoAtual);
    }
//
//    public void validaCpf(String cpf) {
//        RestTemplate restTemplate = new RestTemplate();
//        CPF response = restTemplate.getForObject("https://user-info.herokuapp.com/users/" + cpf, CPF.class);
//
//        if (response.getStatus().equals("UNABLE_TO_VOTE"))  throw new CPFInvalidoException(cpf);
//    }
}
