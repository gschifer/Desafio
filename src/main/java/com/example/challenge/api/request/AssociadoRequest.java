package com.example.challenge.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AssociadoRequest {

    @ApiModelProperty(example = "João da Silva", required = true)
    @NotBlank
    @Column(nullable = false)
    private String nome;

    @ApiModelProperty(example = "087.765.580-27", required = true)
    @NotBlank
    @CPF
    @Column(nullable = false)
    private String cpf;

    @ApiModelProperty(example = "joao@gmail.com", required = true)
    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;
}
