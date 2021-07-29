package com.example.challenge.exceptions.pautaExceptions;

import com.example.challenge.exceptions.NegocioException;

public class PautaSemVotosException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public PautaSemVotosException(String message) {
        super(message);
    }

    public PautaSemVotosException() {
        this("A pauta não recebeu nenhum voto dos associados.");
    }

}
