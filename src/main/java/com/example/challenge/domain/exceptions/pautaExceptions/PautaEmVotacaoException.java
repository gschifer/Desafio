package com.example.challenge.domain.exceptions.pautaExceptions;

import com.example.challenge.domain.exceptions.NegocioException;

public class PautaEmVotacaoException extends NegocioException {
    public PautaEmVotacaoException(String message) {
        super(message);
    }

    public PautaEmVotacaoException(Long id) {
        this(String.format("A pauta de ID: %d, já está em votação.", id));
    }
}
