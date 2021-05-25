package com.example.challenge.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteException {
    private Integer status;

    @JsonFormat(pattern = "dd/MM/YYYY HH:mm:ss")
    private LocalDateTime dataHora;
    private String titulo;
    private List<Campo> campos;

    @AllArgsConstructor
    @Data
    public static class Campo {
        private String nome;
        private String mensagem;
    }
}
