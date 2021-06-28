package com.example.challenge.domain.entities;

import com.example.challenge.enums.PautaEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "pautas")
public class Pauta implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String titulo;

    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL)
    private List<Voto> votos;

    private String status = PautaEnum.ABERTA.getDescricao();

    private String resultado = PautaEnum.INDEFINIDO.getDescricao();

    @JsonFormat(pattern = "dd/MM/YYYY HH:mm:ss")
    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
}
