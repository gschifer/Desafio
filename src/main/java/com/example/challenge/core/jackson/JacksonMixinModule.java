package com.example.challenge.core.jackson;

import com.example.challenge.api.model.mixin.AssociadoMixin;
import com.example.challenge.api.model.mixin.PautaMixin;
import com.example.challenge.api.model.mixin.VotoMixin;
import com.example.challenge.domain.entities.Associado;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.entities.Voto;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

@Component
public class JacksonMixinModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public JacksonMixinModule() {
        setMixInAnnotation(Associado.class, AssociadoMixin.class);
        setMixInAnnotation(Pauta.class, PautaMixin.class);
        setMixInAnnotation(Voto.class, VotoMixin.class);
    }

}