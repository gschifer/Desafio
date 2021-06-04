//package com.example.challenge.services;
//
//import com.example.challenge.domain.entities.Pauta;
//import com.example.challenge.prototype.PautaPrototype;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class PautaServiceTest {
//    @Autowired
//    private PautaService pautaService;
//
//    @Test
//    void deveValidarPauta() {
//        Assertions.assertTrue(pautaService.validaPauta(1L));
//    }
//
//    @Test
//    void deveSalvarPauta() {
//        Pauta pauta = pautaService.savePauta(PautaPrototype.anPauta());
//        Assertions.assertNotNull(pauta);
//        Assertions.assertEquals(PautaPrototype.anPauta().getNome(), pauta.getNome());
//        Assertions.assertEquals(PautaPrototype.anPauta().getResultado(), pauta.getResultado());
//        Assertions.assertEquals(PautaPrototype.anPauta().getStatus(), pauta.getStatus());
//
//    }
//
//    @Test
//    void deveRetornarTodasAsPautas() {
//        Assertions.assertEquals(2, pautaService.getPautas().stream().count());
//    }
//
//    @Test
//    void deveAtualizarApauta() {
//        pautaService.updateResultado(1L);
//    }
//}