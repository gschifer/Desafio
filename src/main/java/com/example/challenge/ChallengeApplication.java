package com.example.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class ChallengeApplication {

    public static void main(String[] args) {
        //Caso não tenha essa linha, fica nesse formato -> 2021-08-05T22:35:05-03:00 e nao com o Z no final
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(ChallengeApplication.class, args);
    }

}
