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
    private String nome;

    @ApiModelProperty(example = "087.765.580-27", required = true)
    @NotBlank
    @CPF
    private String cpf;

    @ApiModelProperty(example = "joao@gmail.com", required = true)
    @NotBlank
    @Email
    private String email;
}
