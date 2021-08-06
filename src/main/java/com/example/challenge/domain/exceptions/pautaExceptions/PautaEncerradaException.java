package com.example.challenge.domain.exceptions.pautaExceptions;

import com.example.challenge.domain.exceptions.NegocioException;

public class PautaEncerradaException extends NegocioException {
    private static final long serialVersionUID = 1L;

    public PautaEncerradaException(String message) {
        super(message);
    }

    public PautaEncerradaException() {
        this("Pauta está encerrada, não são aceitos novos votos.");
    }
}
