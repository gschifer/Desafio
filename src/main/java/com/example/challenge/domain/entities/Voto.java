package com.example.challenge.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "votos")
public class Voto implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(name = "descricao_voto", nullable = false)
    private String descricaoVoto;

    @JsonIgnore
    @ManyToOne
    private Associado associado;

    @JsonIgnore
    @ManyToOne
    private Pauta pauta;
}
