package com.example.challenge.api.dto;

import com.example.challenge.api.model.view.PautaView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class PautaDTO {

    @JsonView(PautaView.Brief.class)
    private Long id;

    @JsonView(PautaView.Brief.class)
    private String titulo;

    @JsonView(PautaView.Brief.class)
    private List<VotoDTO> votos;

    @JsonView(PautaView.Brief.class)
    private String status;

    @JsonView(PautaView.Brief.class)
    private String resultado;

    @JsonView(PautaView.Brief.class)
    private OffsetDateTime createdAt;
}
