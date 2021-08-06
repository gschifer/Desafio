package com.example.challenge.api.exceptionHandler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@ApiModel("Campos")
@Getter
@Builder
public class Field {

    @ApiModelProperty(example = "nome", position = 35)
    private String property;

    @ApiModelProperty(example = " 'nome' é obrigatório.", position = 40)
    private String message;
}
