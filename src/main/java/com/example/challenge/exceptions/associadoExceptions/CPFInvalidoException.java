package com.example.challenge.exceptions.associadoExceptions;

import com.example.challenge.exceptions.NegocioException;

public class CPFInvalidoException extends NegocioException {
    public CPFInvalidoException(String message) {
        super(String.format("O CPF: %s é inválido, não é possível cadastrar o associado.", message));
    }

}
