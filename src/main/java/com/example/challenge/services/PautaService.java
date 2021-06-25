package com.example.challenge.services;

import com.example.challenge.contador.Contador;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.enums.PautaEnum;
import com.example.challenge.enums.VotoEnum;
import com.example.challenge.exceptions.EmptyListException;
import com.example.challenge.exceptions.pautaExceptions.*;
import com.example.challenge.exceptions.ReaberturaInvalidaException;
import com.example.challenge.repository.PautaRepository;
import com.example.challenge.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class PautaService {
    PautaRepository pautaRepository;
    VotoRepository votoRepository;
    AssociadoService associadoService;

    @Autowired
    public PautaService(PautaRepository pautaRepository, VotoRepository votoRepository, AssociadoService associadoService) {
        this.pautaRepository = pautaRepository;
        this.votoRepository = votoRepository;
        this.associadoService = associadoService;
    }

    public Pauta getPauta(Long pautaId) {
        Pauta pauta = pautaRepository.findById(pautaId).orElseThrow(() -> new PautaNaoEncontradaException(pautaId));

        return pauta;
    }

    public void validaPauta(Long pautaId) {
        Pauta pautaExiste = this.getPauta(pautaId);
        String pautaStatus = pautaExiste.getStatus();


        if (pautaStatus.equals(PautaEnum.ABERTA.getDescricao())) throw new PautaInvalidaException();
        if (pautaStatus.equals(PautaEnum.ENCERRADA.getDescricao()))throw new PautaEncerradaException();
    }


    public Pauta reabrePauta(Long pautaId) {
        Pauta pauta = this.getPauta(pautaId);
        List<Voto> votos = votoRepository.findByPautaId(pautaId);

        if (!pauta.getResultado().equals(PautaEnum.EMPATE.getDescricao())) {
            throw new ReaberturaInvalidaException();
        }

        votos.stream().forEach(voto -> votoRepository.deleteById(voto.getId()));
        pauta.setResultado(PautaEnum.INDEFINIDO.getDescricao());
        pauta.setStatus(PautaEnum.ABERTA.getDescricao());

        return this.pautaRepository.save(pauta);

    }


    public void updatePauta(Long pautaId) {
        if (naoPodeProsseguir(pautaId)) return;

        List<Voto> votos = votoRepository.findByPautaId(pautaId);
        Pauta pauta     = getPauta(pautaId);

        Pauta pautaAtualizada = atualizaResultadoDaPauta(pauta, votos);

        pautaAtualizada.setStatus(PautaEnum.ENCERRADA.getDescricao());
        pautaRepository.save(pautaAtualizada);
    }

    private boolean naoPodeProsseguir(Long pautaId) {
        long associadosContagem = associadoService.getAssociados().stream().count();
        long votosContagem = votoRepository.findByPautaId(pautaId).stream().count();

        return associadosContagem == votosContagem;
    }

    private Pauta atualizaResultadoDaPauta(Pauta pauta, List<Voto> votos) {
        long votosAfavor = votos
                .stream()
                .filter(v -> v.getDescricaoVoto().equals(VotoEnum.SIM.getDescricao())).count();

        long votosContra = votos
                .stream()
                .filter(v -> v.getDescricaoVoto().equals(VotoEnum.NAO.getDescricao())).count();

        if (votosAfavor > votosContra) {
            pauta.setResultado(PautaEnum.APROVADA.getDescricao());
        } else if (votosAfavor < votosContra) {
            pauta.setResultado(PautaEnum.REPROVADA.getDescricao());
        } else {
            pauta.setResultado(PautaEnum.EMPATE.getDescricao());
        }

        return pauta;
    }

    @Transactional
    public Pauta savePauta(Pauta pauta) {
        Assert.isNull(pauta.getId());
        return pautaRepository.save(pauta);
    }

    public List<Pauta> getPautas() {
        List<Pauta> pautas = pautaRepository.findAll();

        if (pautas.isEmpty()) {
            throw new EmptyListException("Não há nenhuma pauta cadastrada no momento");
        }

        return pautas;
    }

    public List<Pauta> getPautasEmpatadas() {
        List<Pauta> pautasEmpatadas = pautaRepository.findByResultado(PautaEnum.EMPATE.getDescricao());

        if (pautasEmpatadas.isEmpty()) throw new EmptyListException("Não há nenhuma pauta empatada no momento.");

        return pautasEmpatadas;
    }

    public List<Pauta> getPautasAprovadas() {
        List<Pauta> pautasAprovadas = pautaRepository.findByResultado(PautaEnum.APROVADA.getDescricao());

        if (pautasAprovadas.isEmpty()) throw new EmptyListException("Não há nenhuma pauta aprovada no momento.");

        return pautasAprovadas;
    }

    public List<Pauta> getPautasReprovadas() {
        List<Pauta> pautasReprovadas = pautaRepository.findByResultado(PautaEnum.REPROVADA.getDescricao());

        if (pautasReprovadas.isEmpty()) throw new EmptyListException("Não há nenhuma pauta reprovada no momento.");

        return pautasReprovadas;
    }

    public void deletePauta(Long pautaId) {
        try {
            pautaRepository.deleteById(pautaId);
        } catch (EmptyResultDataAccessException ex) {
            throw new DeletePautaException(pautaId);
        }
    }

    public void colocaPautaEmVotacao(Long pautaId, Integer tempo) {
        if (tempo == null) tempo = 1;

        Pauta pauta = getPauta(pautaId);

        verificaSePodeIrEmVotacao(pauta);
        pauta.setStatus(PautaEnum.EM_VOTACAO.getDescricao());
        pautaRepository.save(pauta);

        new Contador(tempo, pautaId, this);
    }

    private void verificaSePodeIrEmVotacao(Pauta pauta) {
        if (pauta.getStatus().equals(PautaEnum.ABERTA.getDescricao())
                && pauta.getResultado().equals(PautaEnum.INDEFINIDO.getDescricao())) {
            return;
        }
            throw new PautaEmVotacaoException(pauta.getId());
    }

    public void encerraVotacao(Long pautaId) {
        updatePauta(pautaId);
    }


}
