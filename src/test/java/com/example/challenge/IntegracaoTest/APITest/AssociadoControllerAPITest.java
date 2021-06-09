package com.example.challenge.IntegracaoTest.APITest;

import com.example.challenge.repository.AssociadoRepository;
import com.example.challenge.util.DatabaseCleaner;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.hamcrest.Matchers.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class AssociadoControllerAPITest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AssociadoRepository associadoRepository;

    private String path = "api/v1/associados";

    @BeforeEach
    public void init() throws SQLException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = path;

        logaComoAdministrador();
        limpaBancoDeDados();
        insereMassaDeDadosParaTeste();
    }

    public void insereMassaDeDadosParaTeste() throws SQLException {
        Connection connection = dataSource.getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("import.sql"));
    }

    public void limpaBancoDeDados() {
        databaseCleaner.clearTables();
    }

    public void logaComoAdministrador() {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName("admin");
        authScheme.setPassword("123");
        RestAssured.authentication = authScheme;
    }

    @Test
    public void deveRetornarStatus200_quandoBuscarAssociado() {
        RestAssured.given()
                    .accept(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void deveConter3Associados() {
        RestAssured.given()
                    .accept(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .body("", Matchers.hasSize(3),
                            "nome", Matchers.hasItems("Gabriel", "José", "João"));
    }

    @Test
    public void deveRetornar201_AoCriarAssociado() {
        RestAssured.given()
                    .body("{\"nome\": \"Teste\", \"email\": \"teste@hotmail.com\"," +
                            "\"cpf\": \"02857620941\"}")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                .when()
                    .post()
                .then()
                    .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void deveRetornar404_aoBuscarAssociados() {
        associadoRepository.deleteAll();
        RestAssured.given()
                    .accept(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deveRetornarStatus200_eRespostaCorreta_AoConsultarAssociadoExistente() {
        RestAssured.given()
                    .pathParam("associadoId", 1)
                    .accept(ContentType.JSON)
                .when()
                    .get("/{associadoId}")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("nome", equalTo("Gabriel"),
                            "cpf", equalTo("03608898025"),
                            "email", equalTo("gabriel@hotmail.com"));
    }

    @Test
    public void deveRetornar403_UsuarioNaoAutorizadoArealizar_OperacaoDePost() {
        RestAssured.given().auth().basic("user", "123")
                    .body("{\"nome\": \"Teste\", \"email\": \"teste@hotmail.com\"," +
                            "\"cpf\": \"02857620941\"}")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                .when()
                    .post()
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void deveRetornar403_UsuarioNaoAutorizadoArealizar_OperacoesDeDelete() {
        RestAssured.given().auth().basic("user", "123")
                    .pathParam("associadoId", 1)
                    .accept(ContentType.JSON)
                .when()
                    .delete("/{associadoId}")
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void deveRetornar403_UsuarioNaoAutorizadoArealizar_OperacoesDePut() {
        RestAssured.given().auth().basic("user", "123")
                .pathParam("associadoId", 1)
                    .body("{\"nome\": \"Teste\", \"email\": \"teste@hotmail.com\"," +
                            "\"cpf\": \"02857620941\"}")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                .when()
                    .put("/{associadoId}")
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
    }



}
