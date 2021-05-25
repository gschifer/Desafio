package com.example.challenge.exceptions;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class ExceptionError implements Serializable {
    private String error;

    //Needed to show the msg in the response field
    public String getError() {
        return error;
    }
}