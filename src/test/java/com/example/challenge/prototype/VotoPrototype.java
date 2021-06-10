package com.example.challenge.prototype;

import com.example.challenge.domain.entities.Voto;
import com.example.challenge.enums.VotoEnum;

public class VotoPrototype {
    public static Voto anVoto() {
        Voto voto = new Voto();
        voto.setDescricaoVoto(VotoEnum.SIM.getDescricao());

        return voto;
    }
}
