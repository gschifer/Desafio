package com.example.challenge.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel("Problema")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Problem {

    @ApiModelProperty(example = "400", position = 1)
    private Integer status;

    @ApiModelProperty(example = "http://localhost:8081/dados-invalidos", position = 10)
    private String type;

    @ApiModelProperty(example = "Dados inválidos", position = 15)
    private String title;

    @ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.",
            position = 20)
    private String detail;

    @ApiModelProperty(example = "2021-12-01T18:09:02.70844Z", position = 5)
    private LocalDateTime timeStamp;

    //Optional fields
    @ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.",
            position = 25)
    private String userMessage;

    @ApiModelProperty(value = "Lista de objetos ou campos que geraram o erro (opcional)",
            position = 30)
    private List<Field> fields;
}
