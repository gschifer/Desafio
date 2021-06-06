package com.example.challenge.repository;

import com.example.challenge.domain.entities.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {
    List<Pauta> findByResultado(String resultado);
}
