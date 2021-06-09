package com.example.challenge.prototype;

import com.example.challenge.domain.entities.Voto;

public class VotoPrototype {
    public static Voto anVoto() {
        Voto voto = new Voto();
        voto.setDescricaoVoto("Sim");

        return voto;
    }
}
