package com.example.challenge.api.dto;

import com.example.challenge.api.model.view.AssociadoView;
import com.example.challenge.api.model.view.PautaView;
import com.example.challenge.api.model.view.VotoView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class AssociadoDTO {

    @JsonView({PautaView.Brief.class, AssociadoView.Brief.class, AssociadoView.Brief.class, VotoView.Brief.class})
    private Long id;

    @JsonView({PautaView.Brief.class, AssociadoView.Brief.class, AssociadoView.Brief.class, VotoView.Brief.class})
    private String nome;

    @JsonView({PautaView.Brief.class, AssociadoView.Brief.class, AssociadoView.Brief.class, VotoView.Brief.class})
    private String email;

    @JsonView({PautaView.Brief.class, AssociadoView.Brief.class})
    private String cpf;

    @JsonView(AssociadoView.Brief.class)
    private List<VotoDTO> votos;

    @JsonView(AssociadoView.Brief.class)
    private OffsetDateTime createdAt;

    @JsonView(AssociadoView.Brief.class)
    private OffsetDateTime updatedAt;
}
