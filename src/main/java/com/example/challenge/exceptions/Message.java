package com.example.challenge.exceptions;

import lombok.Data;

@Data
public class Message {

    private String timestamp;
    private String error;
    private int status;
    private String exception;
    private String message;
}
