package com.example.challenge.domain.exceptions.associadoExceptions;

import com.example.challenge.domain.exceptions.EntidadeNaoEncontradaException;

public class AssociadoNaoEncontradoException extends EntidadeNaoEncontradaException {
    private static final long serialVersionUID = 1L;

    public AssociadoNaoEncontradoException(String message) {
        super(message);
    }

    public AssociadoNaoEncontradoException(Long id) {
        this(String.format("Não foi encontrado o associado com o ID: %d informado.", id));
    }
}
