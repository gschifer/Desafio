package com.example.challenge.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AssociadoRequest {
    @NotBlank
    @Column(nullable = false)
    private String nome;

    @CPF
    private String cpf;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;
}
