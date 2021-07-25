package com.example.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.challenge"))
                .build()
                .apiInfo(apiInfo())
                .tags(new Tag("Associados", "Gerencia os associados"),
                        new Tag("Pautas", "Gerencia as pautas"),
                        new Tag("Usuários", "Gerencia os usuários"))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, globalGetResponseMessage())
                .globalResponseMessage(RequestMethod.POST, globalPostPutResponseMessage())
                .globalResponseMessage(RequestMethod.PUT, globalPostPutResponseMessage())
                .globalResponseMessage(RequestMethod.DELETE, globalDeleteResponseMessage());
    }

    private List<ResponseMessage> globalDeleteResponseMessage() {
        return Arrays.asList(
                new ResponseMessageBuilder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Erro interno do servidor.")
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Requisição inválida (erro do cliente)")
                        .responseModel(new ModelRef("Problema"))
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                        .message("Método não suportado.")
                        .responseModel(new ModelRef("Problema"))
                        .build()
        );
    }

    private List<ResponseMessage> globalPostPutResponseMessage() {
         return Arrays.asList(
                new ResponseMessageBuilder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Erro interno do servidor.")
                        .build(),
                 new ResponseMessageBuilder()
                         .code(HttpStatus.NOT_ACCEPTABLE.value())
                         .message("A resposta requisitada pelo cliente está em um formato não suportado pelo servidor.")
                         .build(),
                 new ResponseMessageBuilder()
                         .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                         .message("Requisição recusada porque o corpo de requisição está em um formato não suportado.")
                         .responseModel(new ModelRef("Problema"))
                         .build(),
                 new ResponseMessageBuilder()
                         .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                         .message("Método não suportado.")
                         .responseModel(new ModelRef("Problema"))
                         .build()
         );
    }

    private List<ResponseMessage> globalGetResponseMessage() {
        return Arrays.asList(
                new ResponseMessageBuilder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Erro interno do servidor.")
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.NOT_ACCEPTABLE.value())
                        .message("A resposta requisitada pelo cliente está em um formato não suportado pelo servidor.")
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                        .message("Método não suportado.")
                        .responseModel(new ModelRef("Problema"))
                        .build()
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact("Gabriel Schiferdecke", "https://www.linkedin.com/in/gabriel-schiferdecke-540307139/", "gabriel.schifer@hotmail.com"))
                .title("Desafio Técnico Back-end")
                .description("API REST")
                .license("Apache Licence Version 2.0")
                .licenseUrl("https://apache.org")
                .version("1.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}