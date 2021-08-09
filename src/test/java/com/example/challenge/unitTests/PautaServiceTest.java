package com.example.challenge.unitTests;

import com.example.challenge.api.request.PautaRequest;
import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.domain.exceptions.EmptyListException;
import com.example.challenge.domain.exceptions.pautaExceptions.PautaInvalidaException;
import com.example.challenge.domain.exceptions.pautaExceptions.PautaNaoEncontradaException;
import com.example.challenge.domain.exceptions.pautaExceptions.ReaberturaInvalidaException;
import com.example.challenge.domain.repository.AssociadoRepository;
import com.example.challenge.domain.repository.PautaRepository;
import com.example.challenge.domain.repository.VotoRepository;
import com.example.challenge.domain.services.PautaService;
import com.example.challenge.prototype.AssociadoPrototype;
import com.example.challenge.prototype.PautaPrototype;
import com.example.challenge.prototype.VotoPrototype;
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

import static com.example.challenge.api.enums.PautaEnum.EMPATE;
import static com.example.challenge.api.enums.PautaEnum.EM_VOTACAO;
import static com.example.challenge.api.enums.VotoEnum.NAO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {
    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private AssociadoRepository associadoRepository;

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
        when(votoRepository.findByPautaId(anyLong())).thenReturn(Optional.of(votos));
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
        List<Pauta> pautas = populaPautas();

        when(pautaRepository.findByResultado(anyString())).thenReturn(pautas);

        List<Pauta> pautasEmpatadas = pautaService.getPautasEmpatadas();

        assertEquals(pautasEmpatadas, pautas);
        verify(pautaService).getPautasEmpatadas();
    }

    private List<Pauta> populaPautas() {
        List<Pauta> pautas = new ArrayList<>();
        Pauta pauta2 = Pauta.builder().id(2L).titulo("Pauta 2").resultado(EMPATE.getDescricao()).build();
        pauta.setResultado(EMPATE.getDescricao());
        pautas.add(pauta);
        pautas.add(pauta2);

        return pautas;
    }

    @Test
    public void deveBuscarPautasReprovadas() {
        List<Pauta> pautas = populaPautas();

        when(pautaRepository.findByResultado(anyString())).thenReturn(pautas);

        List<Pauta> pautasReprovadas = pautaService.getPautasReprovadas();

        assertEquals(pautasReprovadas, pautas);
        verify(pautaService).getPautasReprovadas();
    }

    @Test
    public void deveBuscarPautasAprovadas() {
        List<Pauta> pautas = populaPautas();

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

    @Test
    public void deveAtualizarPauta() {
        pauta.setStatus(EM_VOTACAO.getDescricao());
        pauta.setVotos(populaVotos());
        Pauta pautaEmVotacao = pauta;

        when(pautaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(PautaPrototype.anPauta()));
        when(pautaRepository.save(any())).thenReturn(pautaEmVotacao);
        when(votoRepository.findByPautaId(anyLong())).thenReturn(Optional.of(populaVotos()));
        when(associadoRepository.findAll()).thenReturn(populaAssociados());

        pautaService.updatePauta(1L);
    }

    private List<Voto> populaVotos() {
        Voto voto2 = Voto.builder().id(2L).descricaoVoto(NAO.getDescricao()).build();
        List<Voto> votos = new ArrayList<>();
        votos.add(voto);
        votos.add(voto2);

        return votos;
    }

    private List<Associado> populaAssociados() {
        Associado associado2 = Associado.builder().id(1L).nome("Leonardo").email("leo@gmail.com").build();
        ArrayList<Associado> associados = new ArrayList<Associado>();
        associados.add(AssociadoPrototype.anAssociado());
        associados.add(associado2);

        return associados;
    }

}
