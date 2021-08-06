package com.example.challenge.api.model.mixin;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Pauta;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class VotoMixin {

    @JsonIgnore
    private Associado associado;

    @JsonIgnore
    private Pauta pauta;
}
