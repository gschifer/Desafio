package com.example.challenge.IntegracaoTest.services;

import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.enums.PautaEnum;
import com.example.challenge.exceptions.EmptyListException;
import com.example.challenge.exceptions.ObjectNotFoundException;
import com.example.challenge.exceptions.PautaEncerradaException;
import com.example.challenge.prototype.PautaPrototype;
import com.example.challenge.repository.PautaRepository;
import com.example.challenge.services.PautaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PautaServiceIT {
    @Autowired
    private PautaService pautaService;

    @Autowired
    private PautaRepository pautaRepository;

    @BeforeEach
    public void unit() {

    }

    @Test
    public void deveRetornarObjectNotFoundException_AoTentarBuscarPautaComIdInexistente() {
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> pautaService.getPauta(10L));
    }

    @Test
    public void deveRetornarIllegalArgumentException_AoTentarCadastrarPauta() {
        Pauta pauta = PautaPrototype.anPauta();
        pauta.setId(10L);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> pautaService.savePauta(pauta));
    }

    @Test
    public void deveCadastrarPautaComSucesso() {
        Pauta pauta = PautaPrototype.anPauta();
        Pauta pautaSalva = pautaService.savePauta(pauta);
        assertThat(pautaSalva, is(notNullValue()));
        Assertions.assertAll(
                () -> assertThat(pautaSalva.getStatus(), is(equalTo(pauta.getStatus()))),
                () -> assertThat(pautaSalva.getResultado(), is(equalTo(pauta.getResultado()))),
                () -> assertThat(pautaSalva.getTitulo(), is(equalTo(pauta.getTitulo())))
        );
    }

    @Test
    public void deveRetornar2Pautas_AoBuscarListaDePautas() {
        assertThat(pautaService.getPautas().size(), is(equalTo(2)));
    }

    @Test
    public void deveBuscarPautaComSucesso() {
        Optional<Pauta> pauta = pautaService.getPauta(1L);
        assertThat(pauta, is(notNullValue()));
        assertThat(pauta.get().getId(), is(equalTo(1L)));
    }

    @Test
    public void deveRetornarEmptyList_AoBuscarListaDePautas() {
        pautaRepository.deleteAll();
        Assertions.assertThrows(EmptyListException.class,
                () -> pautaService.getPautas());
    }

    @Test
    public void deveValidarPautas() {
        pautaService.validaPauta(1L);
        pautaService.validaPauta(2L);
    }

    @Test
    public void deveInvalidarPauta_RetornandoPautaEncerradaException() {
        Pauta pauta = PautaPrototype.anPauta();
        pauta.setStatus(PautaEnum.ENCERRADA.getDescricao());
        Pauta pautaSalva = pautaService.savePauta(pauta);
        Assertions.assertThrows(PautaEncerradaException.class,
                () -> pautaService.validaPauta(pautaSalva.getId()));
    }

    @Test
    public void deveReabrirPautaEmpatada() {
        Optional<Pauta> pauta = pautaService.getPauta(1L);
        pauta.get().setResultado(PautaEnum.EMPATE.getDescricao());
        pautaRepository.save(pauta.get());
        Pauta pautaReaberta = pautaService.reabrePauta(1L);
        assertThat(pautaReaberta, is(notNullValue()));
        assertThat(pautaReaberta.getResultado(), is(equalTo(PautaEnum.INDEFINIDO.getDescricao())));
    }

}
