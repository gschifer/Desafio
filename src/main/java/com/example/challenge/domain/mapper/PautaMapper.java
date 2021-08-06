package com.example.challenge.domain.mapper;

import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.request.PautaRequest;

import static com.example.challenge.api.enums.PautaEnum.ABERTA;
import static com.example.challenge.api.enums.PautaEnum.INDEFINIDO;

public class PautaMapper {
    public static Pauta map(PautaRequest request) {
        return Pauta.builder()
                .titulo(request.getTitulo())
                .status(ABERTA.getDescricao())
                .resultado(INDEFINIDO.getDescricao())
                .build();
    }
}
