package com.example.challenge.api.dto;

import com.example.challenge.domain.entities.Voto;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class PautaDTO {

    private Long id;
    private String titulo;
    private List<Voto> votos;
    private String status;
    private String resultado;
    private OffsetDateTime createdAt;
}
