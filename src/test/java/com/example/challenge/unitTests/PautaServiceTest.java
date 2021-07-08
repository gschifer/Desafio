package com.example.challenge.unitTests;

import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.domain.request.PautaRequest;
import com.example.challenge.exceptions.EmptyListException;
import com.example.challenge.exceptions.pautaExceptions.PautaInvalidaException;
import com.example.challenge.exceptions.pautaExceptions.PautaNaoEncontradaException;
import com.example.challenge.exceptions.pautaExceptions.ReaberturaInvalidaException;
import com.example.challenge.prototype.PautaPrototype;
import com.example.challenge.prototype.VotoPrototype;
import com.example.challenge.repository.PautaRepository;
import com.example.challenge.repository.VotoRepository;
import com.example.challenge.services.PautaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.challenge.enums.PautaEnum.*;
import static com.example.challenge.enums.VotoEnum.NAO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {
    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    @Spy
    private PautaService pautaService;

    private Pauta pauta;

    private PautaRequest pautaRequest;

    private Voto voto;

    @BeforeEach
    public void setup() {
        pauta = PautaPrototype.anPauta();
        pautaRequest = new PautaRequest("Pauta 1");
        voto = VotoPrototype.anVoto();
    }

    @Test
    public void deveBuscarPauta() {
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(pauta));

        pautaService.getPauta(1L);

        verify(pautaService).getPauta(1L);
        verifyNoMoreInteractions(pautaService);
    }

    @Test
    public void deveLancarPautaNaoEncontradaException() {
        when(pautaRepository.findById(anyLong())).thenThrow(PautaNaoEncontradaException.class);

        Executable executable = () -> pautaService.getPauta(3L);

        assertThrows(PautaNaoEncontradaException.class, executable);
        verify(pautaService).getPauta(3L);
    }

    @Test
    public void DeveValidarPauta() {
        pauta.setStatus(EM_VOTACAO.getDescricao());
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(pauta));

        pautaService.validaPauta(1L);

        verify(pautaService).validaPauta(1L);
    }

    @Test
    public void deveLancarPautaInvalidaException() {
        doThrow(PautaInvalidaException.class).when(pautaRepository).findById(anyLong());

        Executable executable = () -> pautaService.validaPauta(3L);
        assertThrows(PautaInvalidaException.class, executable);

        verify(pautaService).validaPauta(3L);
    }

    @Test
    public void deveReabrirPauta() {
        pauta.setResultado(EMPATE.getDescricao());
        Voto voto2 = Voto.builder().id(2L).descricaoVoto(NAO.getDescricao()).build();

        List<Voto> votos = new ArrayList<>();
        votos.add(voto);
        votos.add(voto2);

        Pauta pautaAserReaberta = pauta;

        when(pautaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(pauta));
        when(votoRepository.findByPautaId(anyLong())).thenReturn(votos);
        when(pautaRepository.save(any())).thenReturn(pautaAserReaberta);

        Pauta pautaReaberta = pautaService.reabrePauta(1L);

        assertEquals(pautaReaberta, pauta);
        verify(pautaService).reabrePauta(1L);
    }

    @Test
    public void deveLancarReaberturaInvalidaException() {
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(pauta));

        Executable executable = () -> pautaService.reabrePauta(1L);

        assertThrows(ReaberturaInvalidaException.class, executable);

        verify(pautaService).reabrePauta(1L);
    }

    @Test
    public void deveTrazerPautas() {
        List<Pauta> pautas = new ArrayList<>();
        pautas.add(pauta);

        when(pautaRepository.findAll()).thenReturn(pautas);

        List<Pauta> pautasBuscadas = pautaService.getPautas();

        assertEquals(pautas, pautasBuscadas);
        verify(pautaService).getPautas();
    }

    @Test
    public void deveLancarEmptyListException() {
        when(pautaRepository.findAll()).thenReturn(new ArrayList<>());

       Executable executable = () -> pautaService.getPautas();

        assertThrows(EmptyListException.class, executable);
        verify(pautaService).getPautas();
    }

    @Test
    public void deveSalvarPauta() {
        when(pautaRepository.save(any())).thenReturn(pauta);

        Pauta pautaSalva = pautaService.savePauta(pautaRequest);

        assertEquals(pautaSalva, pauta);
        verify(pautaService).savePauta(pautaRequest);
    }

    @Test
    public void deveBuscarPautasEmpatadas() {
        List<Pauta> pautas = new ArrayList<>();
        Pauta pauta2 = Pauta.builder().id(2L).titulo("Pauta 2").resultado(EMPATE.getDescricao()).build();
        pauta.setResultado(EMPATE.getDescricao());
        pautas.add(pauta);
        pautas.add(pauta2);

        when(pautaRepository.findByResultado(anyString())).thenReturn(pautas);

        List<Pauta> pautasEmpatadas = pautaService.getPautasEmpatadas();

        assertEquals(pautasEmpatadas, pautas);
        verify(pautaService).getPautasEmpatadas();
    }

    @Test
    public void deveBuscarPautasReprovadas() {
        List<Pauta> pautas = new ArrayList<>();
        Pauta pauta2 = Pauta.builder().id(2L).titulo("Pauta 2").resultado(REPROVADA.getDescricao()).build();
        pauta.setResultado(REPROVADA.getDescricao());
        pautas.add(pauta);
        pautas.add(pauta2);

        when(pautaRepository.findByResultado(anyString())).thenReturn(pautas);

        List<Pauta> pautasReprovadas = pautaService.getPautasReprovadas();

        assertEquals(pautasReprovadas, pautas);
        verify(pautaService).getPautasReprovadas();
    }

    @Test
    public void deveBuscarPautasAprovadas() {
        List<Pauta> pautas = new ArrayList<>();
        Pauta pauta2 = Pauta.builder().id(2L).titulo("Pauta 2").resultado(APROVADA.getDescricao()).build();
        pauta.setResultado(APROVADA.getDescricao());
        pautas.add(pauta);
        pautas.add(pauta2);

        when(pautaRepository.findByResultado(anyString())).thenReturn(pautas);

        List<Pauta> pautasAprovadas = pautaService.getPautasAprovadas();

        assertEquals(pautasAprovadas, pautas);
        verify(pautaService).getPautasAprovadas();
    }

    @Test
    public void deveLancarPautaNaoEncontradaExceptionAoExcluir() {
        doThrow(EmptyResultDataAccessException.class).when(pautaRepository).deleteById(anyLong());

        Executable executable = () -> pautaService.deletePauta(1L);

        assertThrows(PautaNaoEncontradaException.class, executable);
        verify(pautaService).deletePauta(1L);
    }

    @Test
    public void deveExcluirPauta() {
        doNothing().when(pautaRepository).deleteById(anyLong());

        pautaService.deletePauta(1L);

        verify(pautaService).deletePauta(1L);
    }

}
