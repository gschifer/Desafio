package com.example.challenge.services;

import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.enums.PautaEnum;
import com.example.challenge.enums.VotoEnum;
import com.example.challenge.exceptions.EmptyListException;
import com.example.challenge.exceptions.ObjectNotFoundException;
import com.example.challenge.exceptions.PautaEncerradaException;
import com.example.challenge.exceptions.ReaberturaInvalida;
import com.example.challenge.repository.PautaRepository;
import com.example.challenge.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

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

    public Optional<Pauta> getPauta(Long pautaId) {
        Optional<Pauta> pauta = pautaRepository.findById(pautaId);

        if (pauta.isEmpty())
            throw new ObjectNotFoundException("A pauta de ID: " + pautaId + " não existe na base de dados.");

        return pauta;
    }

    public void validaPauta(Long pautaId) {
        Optional<Pauta> pautaExiste = this.getPauta(pautaId);
        boolean pautaEncerrada      = pautaExiste.get().getStatus().equals(PautaEnum.ENCERRADA.getDescricao());

        if (pautaEncerrada) throw new PautaEncerradaException("Pauta já encerrada, não são aceitos novos votos.");
    }


    public Pauta reabrePauta(Long pautaId) {
        Optional<Pauta> pauta = this.getPauta(pautaId);
        List<Voto> votos = votoRepository.findByPautaId(pautaId);

        if (!pauta.get().getResultado().equals(PautaEnum.EMPATE.getDescricao())) {
            throw new ReaberturaInvalida("Pauta não está com o resultado 'Empate', não é possível reabrir.");
        }

        votos.stream().forEach(voto -> votoRepository.deleteById(voto.getId()));
        pauta.get().setResultado(PautaEnum.INDEFINIDO.getDescricao());
        pauta.get().setStatus(PautaEnum.ABERTA.getDescricao());

        return this.pautaRepository.save(pauta.get());

    }


    public void updateResultado(Long pautaId) {
        if (naoPodeProsseguir(pautaId)) return;

        List<Voto>      voto  = votoRepository.findByPautaId(pautaId);
        Optional<Pauta> pauta = pautaRepository.findById(pautaId);

        long votosAfavor = voto
                .stream()
                .filter(v -> v.getDescricaoVoto().equals(VotoEnum.SIM.getDescricao())).count();

        long votosContra = voto
                .stream()
                .filter(v -> v.getDescricaoVoto().equals(VotoEnum.NAO.getDescricao())).count();

        if (votosAfavor == votosContra) {
            pauta.get().setResultado(PautaEnum.EMPATE.getDescricao());
        } else if (votosAfavor > votosContra) {
            pauta.get().setResultado(PautaEnum.APROVADA.getDescricao());
        } else {
            pauta.get().setResultado(PautaEnum.REPROVADA.getDescricao());
        }

        pauta.get().setStatus(PautaEnum.ENCERRADA.getDescricao());
        pautaRepository.save(pauta.get());
    }

    private boolean naoPodeProsseguir(Long pautaId) {
        long associadosContagem = associadoService.getAssociados().stream().count();
        long votosContagem = votoRepository.findByPautaId(pautaId).stream().count();

        return associadosContagem > votosContagem;
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
        pautaRepository.deleteById(pautaId);
    }
}
