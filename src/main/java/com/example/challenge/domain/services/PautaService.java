package com.example.challenge.domain.services;

import com.example.challenge.api.enums.VotoEnum;
import com.example.challenge.api.mapper.PautaMapper;
import com.example.challenge.api.mapper.VotoMapper;
import com.example.challenge.api.request.PautaRequest;
import com.example.challenge.api.request.VotoRequest;
import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.domain.exceptions.EmptyListException;
import com.example.challenge.domain.exceptions.pautaExceptions.*;
import com.example.challenge.domain.repository.PautaRepository;
import com.example.challenge.utils.Contador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.challenge.api.enums.PautaEnum.*;

@Service
public class PautaService {
    private PautaRepository pautaRepository;
    private VotoService votoService;
    private AssociadoService associadoService;
    private PautaMapper pautaMapper;
    private VotoMapper votoMapper;

    @Autowired
    public PautaService(PautaRepository pautaRepository, VotoService votoService, AssociadoService associadoService,
                        PautaMapper pautaMapper, VotoMapper votoMapper) {
        this.pautaRepository = pautaRepository;
        this.votoService = votoService;
        this.associadoService = associadoService;
        this.pautaMapper = pautaMapper;
        this.votoMapper = votoMapper;
    }

    public Pauta getPauta(Long pautaId) {
        return pautaRepository.findById(pautaId).orElseThrow(() -> new PautaNaoEncontradaException(pautaId));
    }

    public void validaPauta(Long pautaId) {
        Pauta pauta = getPauta(pautaId);
        String pautaStatus = pauta.getStatus();
        String pautaResultado = pauta.getResultado();

        if (pautaStatus.equals(ABERTA.getDescricao())) {
            throw new PautaInvalidaException();
        }

        if (pautaResultado.equals(ENCERRADA.getDescricao())) {
            throw new PautaEncerradaException();
        }
    }


    @Transactional
    public Pauta reabrePauta(Long pautaId) {
        Pauta pauta = getPauta(pautaId);
        List<Voto> votos = votoService.getVotosByPautaId(pautaId);

        if (!pauta.getResultado().equals(EMPATE.getDescricao())) {
            throw new ReaberturaInvalidaException("Você não pode reabrir uma pauta que não está empatada.");
        }

        excluiVotosDaPauta(votos);
        pauta.setResultado(INDEFINIDO.getDescricao());
        pauta.setStatus(ABERTA.getDescricao());

        return pautaRepository.save(pauta);
    }

    @Transactional
    public void updatePauta(Long pautaId) {
        if (naoPodeProsseguir(pautaId)) return;

        List<Voto> votos = votoService.getVotosByPautaId(pautaId);
        Pauta pauta = getPauta(pautaId);

        Pauta pautaAtualizada = atualizaResultadoDaPauta(pauta, votos);

        pautaAtualizada.setStatus(ENCERRADA.getDescricao());
        pautaRepository.save(pautaAtualizada);
    }

    private boolean naoPodeProsseguir(Long pautaId) {
        long associadosContagem = associadoService.getAssociados().size();
        long votosContagem = votoService.getVotosByPautaId(pautaId).size();

        return associadosContagem == votosContagem;
    }

    private Pauta atualizaResultadoDaPauta(Pauta pauta, List<Voto> votos) {
        long votosAfavor = votos.stream()
                .filter(v -> v.getDescricaoVoto().equals(VotoEnum.SIM.getDescricao())).count();

        long votosContra = votos.stream()
                .filter(v -> v.getDescricaoVoto().equals(VotoEnum.NAO.getDescricao())).count();

        pauta.setResultado(verificaResultado(votosAfavor, votosContra));

        return pauta;
    }

    private String verificaResultado(long votosAfavor, long votosContra) {
        if (votosAfavor > votosContra) return APROVADA.getDescricao();
        else if (votosAfavor < votosContra) return REPROVADA.getDescricao();

        return EMPATE.getDescricao();
    }

    @Transactional
    public Pauta savePauta(PautaRequest pauta) {
        return pautaRepository.save(pautaMapper.map(pauta));
    }

    public List<Pauta> getPautas() {
        List<Pauta> pautas = pautaRepository.findAll();

        if (pautas.isEmpty()) {
            throw new EmptyListException("Não há nenhuma pauta cadastrada no momento");
        }

        return pautas;
    }

    public List<Pauta> getPautasEmpatadas() {
        List<Pauta> pautasEmpatadas = pautaRepository.findByResultado(EMPATE.getDescricao());

        if (pautasEmpatadas.isEmpty()) {
            throw new EmptyListException("Não há nenhuma pauta empatada no momento.");
        }

        return pautasEmpatadas;
    }

    public List<Pauta> getPautasAprovadas() {
        List<Pauta> pautasAprovadas = pautaRepository.findByResultado(APROVADA.getDescricao());

        if (pautasAprovadas.isEmpty()) {
            throw new EmptyListException("Não há nenhuma pauta aprovada no momento.");
        }

        return pautasAprovadas;
    }

    public List<Pauta> getPautasReprovadas() {
        List<Pauta> pautasReprovadas = pautaRepository.findByResultado(REPROVADA.getDescricao());

        if (pautasReprovadas.isEmpty()) {
            throw new EmptyListException("Não há nenhuma pauta reprovada no momento.");
        }

        return pautasReprovadas;
    }

    @Transactional
    public void deletePauta(Long pautaId) {
        try {
            pautaRepository.deleteById(pautaId);
        } catch (EmptyResultDataAccessException ex) {
            throw new PautaNaoEncontradaException(pautaId);
        }
    }

    @Transactional
    public void colocaPautaEmVotacao(Long pautaId, Integer tempo) {
        if (tempo == null) tempo = 1;

        Pauta pauta = getPauta(pautaId);
        verificaSePodeIrEmVotacao(pauta);

        pauta.setStatus(EM_VOTACAO.getDescricao());
        pautaRepository.save(pauta);

        new Contador(tempo, pautaId, this);
    }

    private void verificaSePodeIrEmVotacao(Pauta pauta) {
        if (pauta.getStatus().equals(ABERTA.getDescricao())
                && pauta.getResultado().equals(INDEFINIDO.getDescricao())) {
            return;
        }
        throw new PautaEmVotacaoException(pauta.getId());
    }

    public void encerraVotacao(Long pautaId) {
        updatePauta(pautaId);
    }

    @Transactional
    public Voto votaNaPauta(VotoRequest votoRequest, Long associadoId, Long pautaId) {
        associadoService.validaAssociado(associadoId, pautaId);
        validaPauta(pautaId);

        Voto voto = votoMapper.map(votoRequest);
        Associado associado = associadoService.getAssociado(associadoId);

        voto.setAssociado(associado);
        voto.setPauta(getPauta(pautaId));

        return votoService.saveVoto(voto);
    }

    private void excluiVotosDaPauta(List<Voto> votos) {
        votos.forEach(voto -> votoService.deleteById(voto.getId()));
    }
}
