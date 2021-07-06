package com.example.challenge.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class AssociadoRequest {
    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Size(min = 11, max = 11)
    private String cpf;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;
}
