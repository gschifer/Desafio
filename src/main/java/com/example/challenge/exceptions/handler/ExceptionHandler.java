package com.example.challenge.exceptions.handler;

import com.example.challenge.exceptions.*;
import com.example.challenge.exceptions.associadoExceptions.CPFInvalidoException;
import com.example.challenge.exceptions.pautaExceptions.PautaInvalidaException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_ERRO_USUARIO = "Ocorreu um erro interno inesperado no sistema. Tente novamente, " +
                                         "se o problema persistir, entre em contato com o administrador do sistema.";

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
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {

        ProblemType type    = ProblemType.RECURSO_NAO_ENCONTRADO;
        LocalDateTime time  = LocalDateTime.now();
        String url          = ex.getRequestURL();
        String detail       = String.format("O recurso '%s' que você tentou acessar, não existe.", url);

        Problem problem = createProblemBuilder(status, type, detail, time).
                            mensagemUsuario(MSG_ERRO_USUARIO).build();


        return handleExceptionInternal(ex, problem, headers, status, request);
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

        else if (rootCause instanceof UnrecognizedPropertyException) {
            return handleUnrecognizedPropertyException((UnrecognizedPropertyException) rootCause,
                                                        headers, status, request);
        }

        else if (rootCause instanceof JsonParseException) {
            return handleJsonParseException((JsonParseException) rootCause, headers, status, request);
        }

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    //Tratamento de erro para corpos de requisição com pontuações erradas.
    private ResponseEntity<Object> handleJsonParseException(JsonParseException ex,
                                                            HttpHeaders headers,
                                                            HttpStatus status,
                                                            WebRequest request) {

        ProblemType type   = ProblemType.CARACTER_INVALIDO;
        String detail      = "Verifique o caracter inválido no corpo de requisição.";
        LocalDateTime time = LocalDateTime.now();

        Problem problem  = createProblemBuilder(status, type, detail, time).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            PautaInvalidaException.class
    })
    public ResponseEntity<?> handlePautaInvalidaException(PautaInvalidaException ex,
                                                          WebRequest request) {

        HttpStatus status  = HttpStatus.BAD_REQUEST;
        ProblemType type   = ProblemType.PAUTA_INVALIDA;
        LocalDateTime time = LocalDateTime.now();

        Problem problem = createProblemBuilder(status, type, ex.getMessage(), time).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({UnrecognizedPropertyException.class})
    private ResponseEntity<Object> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex,
                                                                       HttpHeaders headers,
                                                                       HttpStatus status,
                                                                       WebRequest request) {

        String propriedadesValidas = ex.getKnownPropertyIds().stream()
                .filter(e -> e != "id")
                .collect(Collectors.toList()).toString();

        LocalDateTime time          = LocalDateTime.now();
        String propriedadeInvalida  = ex.getPropertyName();

        String detail = String.format("A propriedade '%s' fornecida não existe, você só pode utilizar a(s) seguinte(s) " +
                "propriedade(s) no corpo de requisição: '%s'.", propriedadeInvalida, propriedadesValidas);

         Problem problem = createProblemBuilder(status, ProblemType.PROPRIEDADE_NAO_RECONHECIDA, detail, time)
                                                .mensagemUsuario(MSG_ERRO_USUARIO)
                                                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
                                                          HttpHeaders headers,
                                                          HttpStatus status,
                                                          WebRequest request) {

        String propriedade = ex.getPath().stream()
                .map(e -> e.getFieldName())
                .collect(Collectors.joining("."));

        Object valor        = ex.getValue();
        String tipoInvalido = ex.getValue().getClass().getSimpleName();
        String tipoValido   = ex.getTargetType().getSimpleName();
        LocalDateTime time  = LocalDateTime.now();

        String detail = String.format("Você digitou o valor: '%s', que é de um tipo inválido" +
                        " '%s', para a propriedade '%s', ajuste o valor da propriedade para o tipo '%s'.",
                valor, tipoInvalido, propriedade, tipoValido);

        Problem problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail, time)
                            .mensagemUsuario(MSG_ERRO_USUARIO)
                            .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
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

    //Tratamento para acesso não autorizado à usuarios
    @org.springframework.web.bind.annotation.ExceptionHandler({
            AccessDeniedException.class
    })
    public ResponseEntity<Object> handlingAcessDenied(AccessDeniedException ex,
                                                 WebRequest request) {

        HttpStatus status  = HttpStatus.FORBIDDEN;
        String detail      = "Você não tem permissão de acesso nessa funcionalidade da aplicação.";
        LocalDateTime time = LocalDateTime.now();

        Problem problem = createProblemBuilder(status, ProblemType.ACESSO_NEGADO, detail, time).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers,
                                                        HttpStatus status,
                                                        WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) ex,
                                                                status, headers, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    //Tratamento de erro para tipos de parametros erraos na URI como um get http://localhost:8081/api/v1/associados/h
    private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                                 HttpStatus status,
                                                                 HttpHeaders headers,
                                                                 WebRequest request) {

        ProblemType type              = ProblemType.PARAMETRO_INVALIDO;
        String parametroInvalido      = ex.getValue().toString();
        String tipoParametroInvalido  = ex.getValue().getClass().getSimpleName();
        String parametroCorreto       = ex.getName();
        String tipoParametroCorreto   = ex.getRequiredType().getSimpleName();

        String   detail  = String.format("O parâmetro de URL '%s' recebeu o valor '%s' que é de um tipo '%s' inválido." +
                                            " Corrija colocando um valor compatível com o tipo '%s'.",
                parametroCorreto, parametroInvalido, tipoParametroInvalido, tipoParametroCorreto);

        LocalDateTime time = LocalDateTime.now();

        Problem problem = createProblemBuilder(status, type, detail, time)
                                            .mensagemUsuario(MSG_ERRO_USUARIO)
                                            .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
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

    @org.springframework.web.bind.annotation.ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<?> handleUncaughtExceptions(Exception ex,
                                                     WebRequest request) {

        HttpStatus status  = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType type   = ProblemType.ERRO_DE_SISTEMA;
        LocalDateTime time = LocalDateTime.now();
        String detail      = MSG_ERRO_USUARIO;

        Problem problem = createProblemBuilder(status, type, detail, time).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            AtributoInvalidoException.class
    })
    public ResponseEntity<?> handleAtributoInvalidoException(AtributoInvalidoException ex,
                                                             WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType type = ProblemType.ATRIBUTO_INVALIDO;
        LocalDateTime time = LocalDateTime.now();
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, type, detail, time).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        List<FieldError> erros = ex.getBindingResult().getFieldErrors();
        List<Field> fields = erros.stream()
                .map(fieldError -> {
                            String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());

                            return Field.builder()
                                    .name(fieldError.getField())
                                    .message(message).build();
                        })
                .collect(Collectors.toList());

        ProblemType type   = ProblemType.DADOS_INVALIDOS;
        String detail      = "Um ou mais campos estão com dados inválidos. Tente novamente.";
        LocalDateTime time = LocalDateTime.now();


        Problem problem = createProblemBuilder(status, type, detail, time)
                .campos(fields).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler({
            CPFInvalidoException.class
    })
    public ResponseEntity<?> handleCPFInvalidoException(CPFInvalidoException ex,
                                                        WebRequest request) {
        HttpStatus status  = HttpStatus.BAD_REQUEST;
        ProblemType type   = ProblemType.CPF_INVALIDO;
        String detail      = ex.getMessage();
        LocalDateTime time = LocalDateTime.now();

        Problem problem = createProblemBuilder(status, type, detail, time).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
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
