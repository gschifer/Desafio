package com.example.challenge.exceptions;

public class ReaberturaInvalida extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ReaberturaInvalida(String message) {
        super(message, null, false, false);
    }
}
