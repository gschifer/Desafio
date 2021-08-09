package com.example.challenge.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotoRequest {

    @NotBlank
    @Column(nullable = false)
    private String descricaoVoto;
}

