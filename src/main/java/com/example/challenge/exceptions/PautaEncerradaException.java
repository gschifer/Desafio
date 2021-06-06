package com.example.challenge.exceptions;

public class PautaEncerradaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PautaEncerradaException(String message) {
        super(message, null, false, false);
    }
}
