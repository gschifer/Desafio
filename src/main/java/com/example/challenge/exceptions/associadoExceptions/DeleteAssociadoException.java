package com.example.challenge.exceptions.associadoExceptions;

import com.example.challenge.exceptions.DeleteException;

public class DeleteAssociadoException extends DeleteException {

    private static final long serialVersionUID = 1L;

    public DeleteAssociadoException(String message) {
        super(message);
    }

    public DeleteAssociadoException(Long id) {
        this(String.format("Não existe um associado com o ID: %d para ser removido", id));
    }
}
