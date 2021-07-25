package com.example.challenge.domain.request;

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

    @ApiModelProperty(example = "João da Silva")
    @NotBlank
    @Column(nullable = false)
    private String nome;

    @ApiModelProperty(example = "087.765.580-27")
    @CPF
    private String cpf;

    @ApiModelProperty(example = "joao@gmail.com")
    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;
}
