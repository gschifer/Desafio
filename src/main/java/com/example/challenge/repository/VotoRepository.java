package com.example.challenge.repository;

import com.example.challenge.domain.entities.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    Optional<Voto> findByAssociadoId(Long associadoId);

    Optional<List<Voto>> findByPautaId(Long pautaId);

    Optional<Voto> findByAssociadoIdAndPautaId(Long associadoId, Long pautaId);
}
