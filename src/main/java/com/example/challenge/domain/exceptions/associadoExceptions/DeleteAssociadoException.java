package com.example.challenge.domain.exceptions.associadoExceptions;

import com.example.challenge.domain.exceptions.NegocioException;

public class DeleteAssociadoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public DeleteAssociadoException(String message) {
        super(message);
    }

    public DeleteAssociadoException(Long id) {
        this(String.format("Não existe um associado com o ID: %d para ser removido", id));
    }
}
