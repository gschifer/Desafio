package com.example.challenge.exceptions;

public class DeleteException extends NegocioException {
    public DeleteException(String message) {
        super(message);
    }

    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
