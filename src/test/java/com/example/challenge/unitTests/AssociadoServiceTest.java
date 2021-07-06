package com.example.challenge.unitTests;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.entities.Voto;
import com.example.challenge.domain.request.AssociadoRequest;
import com.example.challenge.domain.request.PautaRequest;
import com.example.challenge.exceptions.VotoInvalidoException;
import com.example.challenge.exceptions.associadoExceptions.AssociadoNaoEncontradoException;
import com.example.challenge.repository.AssociadoRepository;
import com.example.challenge.repository.VotoRepository;
import com.example.challenge.services.AssociadoService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssociadoServiceTest {
    @InjectMocks
    @Spy
    private AssociadoService associadoService;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private VotoRepository votoRepository;

    private Associado associado;

    private AssociadoRequest associadoRequest;

    private Pauta pauta;

    private PautaRequest pautaRequest;

    @BeforeEach
    public void init() {
        associado = Associado.builder()
                .nome("Gabriel")
                .cpf("03607758026")
                .email("gabi@gmail.com")
                .id(1L)
                .build();

        pauta = Pauta.builder()
                .id(1L)
                .titulo("Pauta 1")
                .build();

        pautaRequest = new PautaRequest("Pauta 1");
        associadoRequest = new AssociadoRequest("Gabriel", "03607758026", "gabi@gmail.com");
    }


    @Test
    public void deveCadastrarAssociado() {
        when(associadoRepository.save(any())).thenReturn(associado);

        associadoService.saveAssociado(associadoRequest);

        verify(associadoService).saveAssociado(associadoRequest);
    }

    @Test
    public void deveBuscarAssociado() {
        when(associadoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(associado));

        Associado associadoBuscado = associadoService.getAssociado(1L);

        assertNotNull(associadoBuscado);
        verify(associadoService).getAssociado(1L);
    }

    @Test
    public void deveLancarAssociadoNaoExisteException() {
        doThrow(AssociadoNaoEncontradoException.class).when(associadoRepository).findById(10L);

        Executable executable = () -> associadoService.getAssociado(10L);

        assertThrows(AssociadoNaoEncontradoException.class, executable);
    }

    @Test
    public void deveValidarAssociado() {
        when(votoRepository.findByAssociadoIdAndPautaId(1L, 1L)).thenReturn(Optional.empty());

         associadoService.validaAssociado(1L, 1L);

        verify(associadoService).validaAssociado(1L, 1L);
    }

    @Test
    public void deveLancarVotoInvalido() {
        Voto voto = new Voto(1L, "Sim", associado, pauta);

        doThrow(VotoInvalidoException.class).when(votoRepository).findByAssociadoIdAndPautaId(1L, 1L);

        Executable executable = () -> associadoService.validaAssociado(1L, 1L);

        assertThrows(VotoInvalidoException.class, executable);
    }

}
