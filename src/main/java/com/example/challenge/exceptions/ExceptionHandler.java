package com.example.challenge.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@AllArgsConstructor
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    private MessageSource messageSource;

    //Tratamento de erro para deletes com ID's inexistentes
    @org.springframework.web.bind.annotation.ExceptionHandler({
            EmptyResultDataAccessException.class
    })
    public ResponseEntity errorNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionError("Esse ID não existe para ser removido.")
        );
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
                new ExceptionError("You cannot pass the value in the body")
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

    //Tratamento de erro para válores inválidos tratados pelo Javax Validation
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        DeleteException deleteException    = new DeleteException();
        List<DeleteException.Campo> campos = new ArrayList<>();

        ex.getBindingResult().getAllErrors()
                .stream()
                .forEach(error -> {

            String nome = ((FieldError) error).getField();
            String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());

            campos.add(new DeleteException.Campo(nome, mensagem));
        });

//        for (ObjectError error : ex.getAllErrors()) {
//            String mensagem = error.getDefaultMessage();
//            String nome = ((FieldError) error).getField();
//
//            campos.add(new DeleteException.Campo(nome, mensagem));
//        }

        deleteException.setDataHora(LocalDateTime.now());
        deleteException.setStatus(status.value());
        deleteException.setTitulo("Um ou mais campos estão com os dados incorretos.");
        deleteException.setCampos(campos);
        return handleExceptionInternal(ex, deleteException, headers, status, request);
    }

}
