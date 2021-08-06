package com.example.challenge.domain.exceptions;

public abstract class NegocioException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NegocioException(String message) {
        super(message);
    }

    public NegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}
