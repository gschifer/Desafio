package com.example.challenge.exceptions.handler;

import com.example.challenge.exceptions.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        if (body == null) {
            body = Problem.builder()
                    .status(status.value())
                    .title(status.getReasonPhrase())
                    .detail(ex.getMessage())
                    .timeStamp(LocalDateTime.now()).build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        }

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
                                                          HttpHeaders headers,
                                                          HttpStatus status,
                                                          WebRequest request) {

        String propriedade = ex.getPath()
                                .stream()
                                .map(e -> e.getFieldName())
                                .collect(Collectors.joining("."));

        Object valor        = ex.getValue();
        String tipoInvalido = ex.getValue().getClass().getSimpleName();
        String tipoValido   = ex.getTargetType().getSimpleName();

        String detail = String.format("Você digitou o valor: '%s', que é de um tipo inválido" +
                        " '%s', para a propriedade '%s', ajuste o valor da propriedade para o tipo '%s'.",
                valor, tipoInvalido, propriedade, tipoValido);

        Problem problem = createProblemBuilder(
                            status,
                            ProblemType.MENSAGEM_INCOMPREENSIVEL, detail, LocalDateTime.now()).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    //Tratamento de erro para deletes com ID's inexistentes
    @org.springframework.web.bind.annotation.ExceptionHandler({
            DeleteException.class
    })
    public ResponseEntity handleDeleteException(DeleteException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType type = ProblemType.ENTIDADE_NAO_ENCONTRADA;
        String detail = ex.getMessage();
        LocalDateTime time = LocalDateTime.now();

        Problem problem = createProblemBuilder(status, type, detail, time).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    //Tratamento de erro para busca de Listas vazias
    @org.springframework.web.bind.annotation.ExceptionHandler({
            EmptyListException.class
    })
    public ResponseEntity<?> handleMessage(EmptyListException ex, WebRequest request) {

        HttpStatus status  = HttpStatus.NOT_FOUND;
        ProblemType type   = ProblemType.LISTA_VAZIA;
        String detail      = ex.getMessage();
        LocalDateTime time = LocalDateTime.now();

        Problem problem = createProblemBuilder(status, type, detail, time).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    //Tratamento de erro para busca de id's inexistentes
    @org.springframework.web.bind.annotation.ExceptionHandler({
            EntidadeNaoEncontradaException.class
    })
    public ResponseEntity<?> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex,
                                                         WebRequest request) {

        HttpStatus status  = HttpStatus.NOT_FOUND;
        ProblemType type   = ProblemType.ENTIDADE_NAO_ENCONTRADA;
        String detail      = ex.getMessage();
        LocalDateTime time = LocalDateTime.now();

        Problem problem = createProblemBuilder(status, type, detail, time).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    //Tratamento de erro para campos não necessários no body, como um ID num POST
    @org.springframework.web.bind.annotation.ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity errorBadRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionError("Você passou algum argumento inválido para essa requisição.")
        );
    }

    //Tratamento de erro para métodos HTTP inválidos como um POST nesse endereco http://localhost:8081/api/v1/associados/10
    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                      HttpHeaders headers,
                                                                      HttpStatus status,
                                                                      WebRequest request) {

        ProblemType type   = ProblemType.METODO_NAO_SUPORTADO;
        String detail      = "Você está tentando acessar um método que não está desenvolvido em nosso sistema.";
        LocalDateTime time = LocalDateTime.now();

        Problem problem = createProblemBuilder(status, type, detail, time).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }



    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status,
                                                        ProblemType type,
                                                        String detail,
                                                        LocalDateTime time) {

        Problem.ProblemBuilder problem = Problem.builder()
                .status(status.value())
                .type(type.getUri())
                .title(type.getTitle())
                .detail(detail)
                .timeStamp(time);

        return problem;
    }
}
