package com.example.challenge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PautaEnum {
    //status
    ABERTA(1, "Aberta"),
    ENCERRADA(2, "Encerrada"),
    EM_VOTACAO(3, "Em votação"),
    //resultado
    EMPATE(4, "Empate"),
    APROVADA(5, "Aprovada"),
    REPROVADA(6, "Reprovada"),
    INDEFINIDO(7, "Indefinido");

    private Integer codigo;
    private String descricao;

}
