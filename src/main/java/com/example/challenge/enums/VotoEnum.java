package com.example.challenge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VotoEnum {
    SIM(1, "Sim"),
    NAO(2, "Não");

    private Integer codigo;
    private String descricao;
}
