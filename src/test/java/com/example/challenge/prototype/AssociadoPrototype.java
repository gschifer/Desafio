package com.example.challenge.prototype;

import com.example.challenge.domain.entities.Associado;

public class AssociadoPrototype {
    public static Associado anAssociado() {
        Associado associado = Associado.builder()
                .id(1L)
                .nome("Gabriel")
                .cpf("03607757098")
                .email("gabi@gmail.com")
                .build();

        return associado;
    }
}
