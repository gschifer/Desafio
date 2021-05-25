package com.example.challenge.domain.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
public class Associado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank //prevents from empty or null fields
    @Size(max = 60)
    private String nome;


    private Long votoId;

    private Long pautaId;

    @Email
//    prevents from invalid sintax on emails inputs
//    those validations are done when we're trying to persist to our DB not in the DB, to avoid undesirable erros from the db
    private String email;
}
