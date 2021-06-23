package com.example.challenge.exceptions;

public class ReaberturaInvalidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ReaberturaInvalidaException(String message) {
        super(message, null, false, false);
    }

    public ReaberturaInvalidaException() {
        this("Pauta não está com o resultado 'Empate', não é possível reabrir.");
    }
}
