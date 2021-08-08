package com.example.challenge.domain.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import static com.example.challenge.api.enums.PautaEnum.ABERTA;
import static com.example.challenge.api.enums.PautaEnum.INDEFINIDO;

@Builder
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "pautas")
@NoArgsConstructor
@AllArgsConstructor
public class Pauta implements Serializable {

    private static final long serialVersionUID = 1;

    @ApiModelProperty(example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ApiModelProperty(example = "Pauta para votação de aumento de salário dos gerentes PJ.", required = true)
    @Column(nullable = false)
    private String titulo;

    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL)
    private List<Voto> votos;

    @ApiModelProperty(example = "Aberta")
    private String status = ABERTA.getDescricao();

    @ApiModelProperty(example = "Indefinido")
    private String resultado = INDEFINIDO.getDescricao();

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private OffsetDateTime createdAt;
}
