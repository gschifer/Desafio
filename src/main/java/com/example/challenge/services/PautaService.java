package com.example.challenge.services;

import com.example.challenge.contador.Contador;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.domain.mapper.PautaMapper;
import com.example.challenge.domain.request.PautaRequest;
import com.example.challenge.enums.VotoEnum;
import com.example.challenge.exceptions.EmptyListException;
import com.example.challenge.exceptions.pautaExceptions.*;
import com.example.challenge.repository.PautaRepository;
import com.example.challenge.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.example.challenge.enums.PautaEnum.*;
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


        if (pautaStatus.equals(ABERTA.getDescricao())) throw new PautaInvalidaException();
        if (pautaStatus.equals(ENCERRADA.getDescricao()))throw new PautaEncerradaException();
    }


    public Pauta reabrePauta(Long pautaId) {
        Pauta pauta = this.getPauta(pautaId);
        List<Voto> votos = votoRepository.findByPautaId(pautaId);

        if (!pauta.getResultado().equals(EMPATE.getDescricao())) {
            throw new ReaberturaInvalidaException("Você não pode reabrir uma pauta que não está empatada.");
        }

        votos.stream().forEach(voto -> votoRepository.deleteById(voto.getId()));
        pauta.setResultado(INDEFINIDO.getDescricao());
        pauta.setStatus(ABERTA.getDescricao());

        return this.pautaRepository.save(pauta);

    }


    public void updatePauta(Long pautaId) {
        if (naoPodeProsseguir(pautaId)) return;

        List<Voto> votos = votoRepository.findByPautaId(pautaId);
        Pauta pauta     = getPauta(pautaId);

        Pauta pautaAtualizada = atualizaResultadoDaPauta(pauta, votos);

        pautaAtualizada.setStatus(ENCERRADA.getDescricao());
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
            pauta.setResultado(APROVADA.getDescricao());
        } else if (votosAfavor < votosContra) {
            pauta.setResultado(REPROVADA.getDescricao());
        } else {
            pauta.setResultado(EMPATE.getDescricao());
        }

        return pauta;
    }

    @Transactional
    public Pauta savePauta(PautaRequest pauta) {
        return pautaRepository.save(PautaMapper.map(pauta));
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

        if (pautasEmpatadas.isEmpty()) throw new EmptyListException("Não há nenhuma pauta empatada no momento.");

        return pautasEmpatadas;
    }

    public List<Pauta> getPautasAprovadas() {
        List<Pauta> pautasAprovadas = pautaRepository.findByResultado(APROVADA.getDescricao());

        if (pautasAprovadas.isEmpty()) throw new EmptyListException("Não há nenhuma pauta aprovada no momento.");

        return pautasAprovadas;
    }

    public List<Pauta> getPautasReprovadas() {
        List<Pauta> pautasReprovadas = pautaRepository.findByResultado(REPROVADA.getDescricao());

        if (pautasReprovadas.isEmpty()) throw new EmptyListException("Não há nenhuma pauta reprovada no momento.");

        return pautasReprovadas;
    }

    public void deletePauta(Long pautaId) {
        try {
            pautaRepository.deleteById(pautaId);
        } catch (EmptyResultDataAccessException ex) {
            throw new PautaNaoEncontradaException(pautaId);
        }
    }

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


}
