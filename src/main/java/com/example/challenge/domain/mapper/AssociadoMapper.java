package com.example.challenge.domain.mapper;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.request.AssociadoRequest;

public class AssociadoMapper {
    public static Associado map(AssociadoRequest request) {
       return Associado.builder()
                .cpf(request.getCpf())
                .nome(request.getNome())
                .email(request.getEmail())
                .build();
    }
}
