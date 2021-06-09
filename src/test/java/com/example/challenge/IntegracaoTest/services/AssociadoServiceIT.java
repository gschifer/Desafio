package com.example.challenge.IntegracaoTest.services;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.exceptions.EmptyListException;
import com.example.challenge.exceptions.ObjectNotFoundException;
import com.example.challenge.exceptions.VotoInvalidoException;
import com.example.challenge.prototype.AssociadoPrototype;
import com.example.challenge.prototype.VotoPrototype;
import com.example.challenge.repository.AssociadoRepository;
import com.example.challenge.services.AssociadoService;
import com.example.challenge.services.PautaService;
import com.example.challenge.services.VotoService;
import com.example.challenge.util.DatabaseCleaner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class AssociadoServiceIT {

    @Autowired
    private AssociadoService associadoService;

    @Autowired
    private PautaService pautaService;

    @Autowired
    private VotoService votoService;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void init() throws SQLException {
        limpaBancoDeDados();
        insereMassaDeDadosParaTeste();
    }

    public void insereMassaDeDadosParaTeste() throws SQLException {
        Connection connection = dataSource.getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("import.sql"));
    }

    public void limpaBancoDeDados() {
        databaseCleaner.clearTables();
    }


    @Test
    public void deveCadastrarVoto() {
       associadoService.validaAssociado(1L, 2L);
        pautaService.validaPauta(2L);

        Voto voto = VotoPrototype.anVoto();
        voto.setAssociado(associadoService.getAssociado(1L).get());
        voto.setPauta(pautaService.getPauta(2L).get());
        Voto votoSalvo = votoService.saveVoto(voto);

        assertThat(votoSalvo, is(notNullValue()));
        Assertions.assertAll(
                () -> assertThat(votoSalvo.getAssociado(), is(equalTo(voto.getAssociado()))),
                () -> assertThat(votoSalvo.getDescricaoVoto(), is(equalTo(voto.getDescricaoVoto()))),
                () -> assertThat(votoSalvo.getPauta(), is(equalTo(voto.getPauta())))
        );
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
        Assertions.assertDoesNotThrow(() -> associadoService.validaAssociado(1L, 2L));
    }

    @Test
    public void deveRetornarVotoInvalidoException() {
        Assertions.assertThrows(VotoInvalidoException.class,
                () -> associadoService.validaAssociado(1L, 1L));
    }
}
