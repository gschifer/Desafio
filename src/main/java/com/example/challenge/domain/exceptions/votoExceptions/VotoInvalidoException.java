package com.example.challenge.domain.exceptions.votoExceptions;

public class VotoInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public VotoInvalidoException(String message) {
        super(message, null, false, false);
    }
}
