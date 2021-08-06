package com.example.challenge.domain.exceptions;

import lombok.Data;

@Data
public class Message {

    private String timestamp;
    private String error;
    private Integer status;
    private String exception;
    private String message;
}
