package com.example.challenge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PautaEnum {
    ABERTA(1, "Aberta"),
    ENCERRADA(2, "Encerrada"),
    EMPATE(3, "Empate"),
    APROVADA(4, "Aprovada"),
    REPROVADA(5, "Reprovada"),
    INDEFINIDO(6, "Indefinido");

    private Integer codigo;
    private String descricao;

}
