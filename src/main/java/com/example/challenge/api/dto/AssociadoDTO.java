package com.example.challenge.api.dto;

import com.example.challenge.domain.entities.Voto;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class AssociadoDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private List<Voto> votos;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
