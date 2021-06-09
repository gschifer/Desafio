package com.example.challenge.IntegracaoTest.APITest;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssociadoCadastro {

    @LocalServerPort
    private int port;

    private String path = "api/v1/associados";

    @BeforeEach
    public void init() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = this.port;
        RestAssured.basePath = this.path;
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
                    .body("", Matchers.hasSize(3))
                    .body("nome", Matchers.hasItems("Gabriel", "José", "João"));
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
}
