package com.example.challenge.prototype;

import com.example.challenge.domain.entities.Voto;

import static com.example.challenge.api.enums.VotoEnum.SIM;

public class VotoPrototype {
    public static Voto anVoto() {
        return Voto.builder()
                .id(1L)
                .descricaoVoto(SIM.getDescricao())
                .build();
    }
}
