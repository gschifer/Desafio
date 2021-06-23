package com.example.challenge.exceptions;

import lombok.Data;
import lombok.Getter;

@Data
public class Message {

    private String timestamp;
    private String error;
    private Integer status;
    private String exception;
    private String message;
}
