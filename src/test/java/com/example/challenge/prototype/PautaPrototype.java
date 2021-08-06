package com.example.challenge.prototype;

import com.example.challenge.domain.entities.Pauta;

import static com.example.challenge.api.enums.PautaEnum.ABERTA;
import static com.example.challenge.api.enums.PautaEnum.INDEFINIDO;

public class PautaPrototype {
    public static Pauta anPauta() {
        return Pauta.builder()
                .id(1L)
                .titulo("Pauta 1")
                .resultado(INDEFINIDO.getDescricao())
                .status(ABERTA.getDescricao())
                .build();
    }
}
