package com.example.challenge.exceptions;

import lombok.Getter;

@Getter
public enum ProblemType {
    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada."),
    LISTA_VAZIA("/lista-vazia", "Lista vazia, não há conteúdo para ser exibido."),
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível, verifique sintaxe."),
    METODO_NAO_SUPORTADO("/metodo-nao-suportado", "Método não suportado."),
    ACESSO_NEGADO("/acesso-negado", "Acesso negado."),
    PROPRIEDADE_NAO_RECONHECIDA("/propriedade-nao-reconhecida", "Propriedade não reconhecida."),
    CARACTER_INVALIDO("/caracter-invalido", "Caracter inválido"),
    PAUTA_INVALIDA("/pauta-invalida", "Pauta inválida."),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido."),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado."),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema."),
    DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos."),
    ATRIBUTO_INVALIDO("/atributo-invalido", "Atributo inválido."),
    CPF_INVALIDO("/cpf-invalido", "CPF inválido.");

    private String uri;
    private String title;

    ProblemType(String uri, String title) {
        this.uri = "https://localhost:8081" + uri;
        this.title = title;
    }
}
