package com.example.challenge.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    //Tratamento de erro para acesso negado à usuários
    @org.springframework.web.bind.annotation.ExceptionHandler({
            AccessDeniedException.class
    })
    @ResponseBody
    public ResponseEntity deniedAccessException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ExceptionError("Você não tem permissão para realizar essa operação.")
        );
    }

    //Tratamento de erro para busca de Listas vazias
    @org.springframework.web.bind.annotation.ExceptionHandler({
            EmptyListException.class
    })
    public ResponseEntity<?> handleMessage(EmptyListException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType type = ProblemType.LISTA_VAZIA;
        String detail = ex.getMessage();
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

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType type  = ProblemType.ENTIDADE_NAO_ENCONTRADA;
        String detail     = ex.getMessage();
        LocalDateTime time = LocalDateTime.now();

        Problem problem = createProblemBuilder(status, type, detail, time).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return super.handleHttpMessageNotReadable(ex, headers, status, request);
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

        return new ResponseEntity<>(new ExceptionError("Operação não permitida."), HttpStatus.METHOD_NOT_ALLOWED);
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
