package com.example.challenge.exceptions;

import lombok.Getter;

@Getter
public enum ProblemType {
    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada."),
    LISTA_VAZIA("/lista-vazia", "Lista vazia, não há conteúdo para ser exibido."),
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível, verifique sintaxe."),
    METODO_NAO_SUPORTADO("/metodo-nao-suportado", "Método não suportado."),
    ACESSO_NEGADO("/acesso-negado", "Acesso negado.");

    private String uri;
    private String title;

    ProblemType(String uri, String title) {
        this.uri = "https://localhost:8081" + uri;
        this.title = title;
    }
}
