package com.example.challenge.unitTests;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.mapper.AssociadoMapper;
import com.example.challenge.domain.request.AssociadoRequest;
import com.example.challenge.domain.request.PautaRequest;
import com.example.challenge.exceptions.EmptyListException;
import com.example.challenge.exceptions.VotoInvalidoException;
import com.example.challenge.exceptions.associadoExceptions.AssociadoNaoEncontradoException;
import com.example.challenge.prototype.AssociadoPrototype;
import com.example.challenge.prototype.PautaPrototype;
import com.example.challenge.repository.AssociadoRepository;
import com.example.challenge.repository.VotoRepository;
import com.example.challenge.services.AssociadoService;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void setup() {
        associado = AssociadoPrototype.anAssociado();
        pauta = PautaPrototype.anPauta();

        pautaRequest = new PautaRequest("Pauta 1");
        associadoRequest = new AssociadoRequest("Gabriel", "03607758026", "gabi@gmail.com");
    }


    @Test
    public void deveCadastrarAssociado() {
        when(associadoRepository.save(any())).thenReturn(associado);

        associadoService.saveAssociado(associadoRequest);

        verify(associadoService).saveAssociado(associadoRequest);
        verifyNoMoreInteractions(associadoService);
    }

    @Test
    public void deveBuscarAssociado() {
        when(associadoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(associado));

        Associado associadoBuscado = associadoService.getAssociado(1L);

        assertNotNull(associadoBuscado);
        verify(associadoService).getAssociado(1L);
        verifyNoMoreInteractions(associadoService);
    }

    @Test
    public void deveLancarAssociadoNaoExisteException() {
        doThrow(AssociadoNaoEncontradoException.class).when(associadoRepository).findById(10L);

        Executable executable = () -> associadoService.getAssociado(10L);

        verifyNoMoreInteractions(associadoService);
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
        doThrow(VotoInvalidoException.class).when(votoRepository).findByAssociadoIdAndPautaId(1L, 1L);

        Executable executable = () -> associadoService.validaAssociado(1L, 1L);

        assertThrows(VotoInvalidoException.class, executable);
    }

    @Test
    public void deveBuscarAssociados() {
        Associado associado2 = Associado.builder()
                .id(2L)
                .email("joao@gmail.com")
                .nome("João")
                .build();

        List<Associado> associados = new ArrayList<>();
        associados.add(associado);
        associados.add(associado2);
        when(associadoRepository.findAll()).thenReturn(associados);

        associadoService.getAssociados();
        verify(associadoService).getAssociados();
    }

    @Test
    public void deveLancarEmptyListException() {
        when(associadoRepository.findAll()).thenThrow(EmptyListException.class);
        Executable executable = () -> associadoService.getAssociados();

        assertThrows(EmptyListException.class, executable);
        verify(associadoService).getAssociados();
    }

    @Test
    public void deveExcluirAssociado() {
        doNothing().when(associadoRepository).deleteById(anyLong());

        associadoService.deleteAssociado(1L);

        verify(associadoService).deleteAssociado(1L);
    }

    @Test
    public void deveLancarAssociadoNaoEncontrado() {
        doThrow(EmptyResultDataAccessException.class).when(associadoRepository).deleteById(anyLong());

        Executable executable = () -> associadoService.deleteAssociado(3L);

        assertThrows(AssociadoNaoEncontradoException.class, executable);
        verify(associadoService).deleteAssociado(3L);
    }

    @Test
    public void deveAtualizarAssociado() {
        AssociadoRequest associadoAtualizado = associadoRequest;
        associadoAtualizado.setEmail("gabriel@gmail.com");

        when(associadoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(associado));
        when(associadoRepository.save(any())).thenReturn(AssociadoMapper.map(associadoAtualizado));

        associadoService.updateAssociado(1L, associadoAtualizado);

        verify(associadoService, times(1)).updateAssociado(1L, associadoRequest);
    }

}
