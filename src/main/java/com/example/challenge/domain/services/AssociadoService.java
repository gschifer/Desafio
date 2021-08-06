package com.example.challenge.domain.services;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.domain.exceptions.EmptyListException;
import com.example.challenge.domain.exceptions.associadoExceptions.AssociadoNaoEncontradoException;
import com.example.challenge.domain.exceptions.votoExceptions.VotoInvalidoException;
import com.example.challenge.domain.mapper.AssociadoMapper;
import com.example.challenge.domain.repository.AssociadoRepository;
import com.example.challenge.domain.repository.VotoRepository;
import com.example.challenge.domain.request.AssociadoRequest;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    public AssociadoService(AssociadoRepository associadoRepository, VotoRepository votoRepository) {
        this.associadoRepository = associadoRepository;
        this.votoRepository = votoRepository;
    }

    public Associado getAssociado(Long associadoId) {
        return associadoRepository.findById(associadoId).orElseThrow(() -> new AssociadoNaoEncontradoException(associadoId));
    }

    public void validaAssociado(Long associadoId, Long pautaId) {
        Optional<Voto> voto = votoRepository.findByAssociadoIdAndPautaId(associadoId, pautaId);

        if (voto.isPresent()) throw new VotoInvalidoException("Associado já votou nesta pauta.");
    }

    @Transactional
    public Associado saveAssociado(AssociadoRequest request) {
        return associadoRepository.save(AssociadoMapper.map(request));
    }

    public List<Associado> getAssociados() {
        List<Associado> associados = associadoRepository.findAll();

        if (associados.isEmpty()) throw new EmptyListException("Não há associados cadastrados no momento.");

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
    public Associado updateAssociado(Long associadoId, Associado associadoRequest) {
        associadoRequest.setId(associadoId);
        Associado associado = getAssociado(associadoId);

        BeanUtils.copyProperties(associadoRequest, associado, "id", "votos", "createdAt ");

        return associadoRepository.save(associadoRequest);

    }
//
//    public void validaCpf(String cpf) {
//        RestTemplate restTemplate = new RestTemplate();
//        CPF response = restTemplate.getForObject("https://user-info.herokuapp.com/users/" + cpf, CPF.class);
//
//        if (response.getStatus().equals("UNABLE_TO_VOTE"))  throw new CPFInvalidoException(cpf);
//    }
}
