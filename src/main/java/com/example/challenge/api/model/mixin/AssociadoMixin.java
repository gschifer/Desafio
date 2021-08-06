package com.example.challenge.api.model.mixin;

import java.time.OffsetDateTime;

public abstract class AssociadoMixin {

    //    Formatação fora do padrão, o modo atual entrega no padrão UTC correto, utilizando OffsetDatetime para gerar o Z
//    @JsonFormat(pattern = "dd/MM/YYYY HH:mm:ss")
    private OffsetDateTime createdAt;

    //    @JsonFormat(pattern = "dd/MM/YYYY HH:mm:ss")
    private OffsetDateTime updatedAt;
}
