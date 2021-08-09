package com.example.challenge.api.dto;

import com.example.challenge.api.model.view.AssociadoView;
import com.example.challenge.api.model.view.PautaView;
import com.example.challenge.api.model.view.VotoView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotoDTO {

    @JsonView({PautaView.Brief.class, AssociadoView.Brief.class, VotoView.Brief.class})
    private Long id;

    @JsonView({PautaView.Brief.class, AssociadoView.Brief.class, VotoView.Brief.class})
    private String descricaoVoto;

    @JsonView({PautaView.Brief.class, VotoView.Brief.class})
    private AssociadoDTO associado;
}
