package com.example.challenge.prototype;

import com.example.challenge.domain.entities.Associado;

public class AssociadoPrototype {
    public static Associado anAssociado() {
        Associado associado = new Associado();
        associado.setNome("Gabriel");
        associado.setEmail("gabi@hotmail.com");
        associado.setCpf("02692586301");

        return associado;
    }
}
