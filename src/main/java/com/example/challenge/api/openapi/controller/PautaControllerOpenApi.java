package com.example.challenge.api.openapi.controller;

import com.example.challenge.api.exceptionHandler.Problem;
import com.example.challenge.domain.entities.Pauta;
import com.example.challenge.domain.request.PautaRequest;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Pautas")
public interface PautaControllerOpenApi {
    
    @ApiOperation("Busca todas as pautas cadastradas.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Não há pautas cadastradas no momento", response = Problem.class)
    })
     ResponseEntity<List<Pauta>> getPautas();

    
    
    @ApiOperation("Busca a pauta desejada pelo ID informado.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Pauta não encontrada", response = Problem.class)
    })
     ResponseEntity<Pauta> getPauta(@ApiParam(value = "ID da pauta", example = "1") Long pautaId);

    
    
    @ApiOperation("Deleta a pauta pelo ID informado.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Pauta excluída com sucesso"),
            @ApiResponse(code = 404, message = "Pauta não encontrada", response = Problem.class)
            
    })
     ResponseEntity deletePauta(@ApiParam(value = "ID da pauta", example = "1", required = true) Long pautaId);

    
    
    @ApiOperation("Busca todas as pautas que estão com o resultado: 'Empatada'.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Não há nenhuma pauta empatada no momento.", response = Problem.class)
    })
     ResponseEntity<List<Pauta>> getPautasEmpatadas();

    
    
    @ApiOperation("Busca todas as pautas que estão com o resultado: 'Aprovada'.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Não há nenhuma pauta aprovada no momento.", response = Problem.class)
    })
     ResponseEntity<List<Pauta>> getPautasAprovadas();

    
    
    @ApiOperation("Busca todas as pautas que estão com o resultado: 'Reprovada'.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Não há nenhuma pauta reprovada no momento.", response = Problem.class)
    })
     ResponseEntity<List<Pauta>> getPautasReprovadas();

    
    
    @ApiOperation("Reabre uma pauta a partir do ID informado.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Não há nenhuma pauta reprovada no momento.", response = Problem.class)
    })
     ResponseEntity<Pauta> reabrePauta(@ApiParam(value = "ID da pauta", example = "1", required = true) Long pautaId);

    
    
    @ApiOperation("Coloca uma pauta em votação por um tempo específico determinado. Obs: Por padrão está como 1 min " +
            "caso não houver um tempo específico determinado por parâmetro.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Pauta não encontrada.", response = Problem.class),
            @ApiResponse(code = 409, message = "Pauta já está em votação", response = Problem.class)
    })
    void colocaPautaEmVotacao(@ApiParam(value = "ID da pauta", example = "1", required = true) Long pautaId,
                              @ApiParam(value = "Tempo de abertura da pauta em minutos. Exemplo: 30 minutos de abertura" +
                                      " é equivalente à: api/v1/pautas/{pautaId}/abrirVotacao?tempo=30") Integer tempo);



    @ApiOperation("Cadastra uma nova pauta.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pauta criada com sucesso.")
    })
     ResponseEntity<Pauta> salvaPauta(@ApiParam(name = "Corpo de requisição", value = "Representação de uma nova pauta",
            required = true) PautaRequest pautaRequest);
}
