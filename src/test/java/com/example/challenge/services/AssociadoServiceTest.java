//package com.example.challenge.services;
//
//import com.example.challenge.domain.entities.Associado;
//import com.example.challenge.exceptions.ObjectNotFoundException;
//import com.example.challenge.prototype.AssociadoPrototype;
//import com.example.challenge.repository.AssociadoRepository;
//import com.example.challenge.repository.VotoRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//import java.util.Optional;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//class AssociadoServiceTest {
//
//    @Autowired
//    private AssociadoService associadoService;
//
//    @Test
//    public void saveAssociado() {
//        Associado associado = AssociadoPrototype.anAssociado();
//        Associado assoc = associadoService.saveAssociado(associado);
//        Assertions.assertNotNull(assoc, "Henrique");
//        Optional<Associado> searchedAssoc = associadoService.getAssociado(assoc.getId());
//        Assertions.assertEquals(assoc, searchedAssoc.get());
//    }
//
//    @Test
//    void getAssociado() {
//        Optional<Associado> associado = associadoService.getAssociado(1L);
//        Assertions.assertEquals("joao", associado.get().getNome());
//    }
//
//    @Test
//    void getAssociados() {
//        List<Associado> associados = associadoService.getAssociados();
//        Assertions.assertEquals(3, associados.stream().count());
//    }
//
//    @Test
//    void validaAssociado() {
//        boolean associado = associadoService.validaAssociado(2L);
//        Assertions.assertEquals(true, associado);
//    }
//
//    @Test
//    void associadoNaoPodeVotar() {
//        boolean associado = associadoService.validaAssociado(1L);
//        Assertions.assertEquals(false, associado);
//    }
//
//
//    @Test
//    void deleteAssociado() {
//        associadoService.deleteAssociado(1L);
//        Assertions.assertThrows(ObjectNotFoundException.class, () -> associadoService.getAssociado(1L));
//    }
//
//    @Test
//    void updateAssociado() {
//        Associado associado = new Associado();
//        associado.setNome("Ronaldo");
//        associado.setEmail("ronaldo@hotmail.com");
//        Associado associadoUpdated = associadoService.updateAssociado(3L, associado);
//
//        Assertions.assertEquals(associado.getEmail(), associadoUpdated.getEmail());
//        Assertions.assertEquals(associado.getNome(), associadoUpdated.getNome());
//
//    }
//}