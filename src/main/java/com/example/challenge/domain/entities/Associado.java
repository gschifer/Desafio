package com.example.challenge.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "associados")
@NoArgsConstructor
@AllArgsConstructor
public class Associado implements Serializable {

    private static final long serialVersionUID = 1;

    @ApiModelProperty(example = "1")
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(example = "João da Silva", required = true)
    private String nome;

    @ApiModelProperty(example = "joao@gmail.com", required = true)
    private String email;

    @ApiModelProperty(example = "087.765.580-27", required = true)
    private String cpf;

    @OneToMany(mappedBy = "associado", cascade = CascadeType.ALL)
    private List<Voto> votos;

    @JsonFormat(pattern = "dd/MM/YYYY HH:mm:ss")
    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    @JsonFormat(pattern = "dd/MM/YYYY HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
