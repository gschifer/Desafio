package com.example.challenge.domain.exceptions.pautaExceptions;

import com.example.challenge.domain.exceptions.NegocioException;

public class PautaSemVotosException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public PautaSemVotosException(String message) {
        super(message);
    }

    public PautaSemVotosException() {
        this("A pauta não recebeu nenhum voto dos associados.");
    }

}
