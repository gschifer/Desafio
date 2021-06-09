package com.example.challenge.prototype;

import com.example.challenge.domain.entities.Pauta;

public class PautaPrototype {
    public static Pauta anPauta() {
        Pauta pauta = new Pauta();
        pauta.setTitulo("Pauta teste");

        return pauta;
    }
}
