package com.grupo1.infraestructure.repository;

import com.grupo1.infraestructure.entity.NombreAnalisisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NombreAnalisisRepository extends JpaRepository<NombreAnalisisEntity, Long> {

    Optional<NombreAnalisisEntity> findByAnalisis (String nombreAnalisis);
}
