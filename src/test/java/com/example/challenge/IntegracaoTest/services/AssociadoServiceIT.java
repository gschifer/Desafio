package com.example.challenge.IntegracaoTest.services;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.exceptions.EmptyListException;
import com.example.challenge.exceptions.ObjectNotFoundException;
import com.example.challenge.exceptions.VotoInvalidoException;
import com.example.challenge.prototype.AssociadoPrototype;
import com.example.challenge.repository.AssociadoRepository;
import com.example.challenge.services.AssociadoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AssociadoServiceIT {

    @Autowired
    private AssociadoService associadoService;
    @Autowired
    private AssociadoRepository associadoRepository;

    @BeforeEach
    public void init() {

    }

    @Test
    public void deveCadastrarAssociado() {
        Associado associado = AssociadoPrototype.anAssociado();
        Associado associadoSalvo = associadoService.saveAssociado(associado);
        assertThat(associadoSalvo, is(notNullValue()));
        Assertions.assertAll(
                () -> assertThat(associadoSalvo.getNome(), is(equalTo(associado.getNome()))),
                () -> assertThat(associadoSalvo.getEmail(), is(equalTo(associado.getEmail()))),
                () -> assertThat(associadoSalvo.getCpf(), is(equalTo(associado.getCpf())))
        );
    }

    @Test
    public void deveRetornarEmptyListException() {
        associadoRepository.deleteAll();
        Assertions.assertThrows(EmptyListException.class,
                () -> associadoService.getAssociados());
    }

    @Test
    public void deveRetornarMensagemQueUsuario_naoExisteNaBaseDeDados() {
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> associadoService.getAssociado(10L));
    }

    @Test
    public void deveImpedirCadastroDoAssociado() {
        Associado associado = AssociadoPrototype.anAssociado();
        associado.setId(1L);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> associadoService.saveAssociado(associado));
    }

    @Test
    public void deveRetornarNumeroDeAssociadosCadastrados_IgualA_3() {
        List<Associado> associados = associadoService.getAssociados();
        assertThat(associados.size(), is(equalTo(3)));
    }

    @Test
    public void deveExcluirAssociado() {
        associadoService.deleteAssociado(1L);
        Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> associadoService.deleteAssociado(1L));
    }

    @Test
    public void deveAtualizarDadosDoAssociado() {
        Associado associado = AssociadoPrototype.anAssociado();
        Associado associadoAtualizado = associadoService.updateAssociado(2L, associado);
        assertThat(associadoAtualizado, is(notNullValue()));
        Assertions.assertAll(
                () -> assertThat(associadoAtualizado.getNome(), is(equalTo(associado.getNome()))),
                () -> assertThat(associadoAtualizado.getEmail(), is(equalTo(associado.getEmail()))),
                () -> assertThat(associadoAtualizado.getCpf(), is(equalTo(associado.getCpf())))
        );
    }

    @Test
    public void deveValidarAssociadoParaVotar() {
        associadoService.validaAssociado(1L, 2L);
    }

    @Test
    public void deveRetornarVotoInvalidoException() {
        Assertions.assertThrows(VotoInvalidoException.class,
                () -> associadoService.validaAssociado(1L, 1L));
    }
}
