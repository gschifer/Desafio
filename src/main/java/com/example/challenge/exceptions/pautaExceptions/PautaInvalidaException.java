package com.example.challenge.exceptions.pautaExceptions;

import com.example.challenge.exceptions.NegocioException;

public class PautaInvalidaException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public PautaInvalidaException(String message) {
        super(message);
    }

    public PautaInvalidaException() {
        this("A pauta não está aceitando votos, pois ainda não está com o status de: 'Em votação'.");
    }

}
