package com.example.challenge.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class PautaRequest {
    @NotBlank
    private String titulo;
}
