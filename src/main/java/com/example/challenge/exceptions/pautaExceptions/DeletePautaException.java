package com.example.challenge.exceptions.pautaExceptions;

import com.example.challenge.exceptions.NegocioException;

public class DeletePautaException extends NegocioException {
    private static final long serialVersionUID = 1L;

    public DeletePautaException(String message) {
        super(message);
    }

    public DeletePautaException(Long id) {
        this(String.format("Não existe uma pauta com o ID: %d especificado.", id));
    }
}
