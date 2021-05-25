package com.example.challenge.controllers;

import com.example.challenge.domain.entities.Associado;
import com.example.challenge.services.AssociadoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class AssociadoController {
    AssociadoService associadoService;

    @GetMapping("/associados")
    public ResponseEntity getAssociados() {
        List<Associado> associados = associadoService.getAssociados();

        return associados.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(associados);
    }

    @GetMapping("/associados/{associadoId}")
    public ResponseEntity getAssociado(@PathVariable Long associadoId) {
        Optional<Associado> associado = associadoService.getAssociado(associadoId);

        return ResponseEntity.ok(associado);
    }

    @PostMapping("/associados")
    public ResponseEntity salvaAssociado(@RequestBody @Valid Associado associado) {
        Associado associadoAux = associadoService.saveAssociado(associado);
        URI location = getUri(associadoAux.getId());

        return ResponseEntity.created(location).body(associadoAux);

    }

    @PostMapping("/associados/verificaCpf")
    public ResponseEntity<Object> verificaCpf(@RequestBody String cpf) {
        RestTemplate restTemplate = new RestTemplate();
        Object response = restTemplate.getForObject("https://user-info.herokuapp.com/users/" + cpf, String.class);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("associados/{associadoId}")
    public ResponseEntity deleteAssociado(@PathVariable Long associadoId) {
        associadoService.deleteAssociado(associadoId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("associados/{associadoId}")
    public ResponseEntity updateAssociado(@PathVariable Long associadoId,
                                          @RequestBody Associado associado) {
        Associado associadoReturn = associadoService.updateAssociado(associadoId, associado);

        return ResponseEntity.ok(associadoReturn);
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
