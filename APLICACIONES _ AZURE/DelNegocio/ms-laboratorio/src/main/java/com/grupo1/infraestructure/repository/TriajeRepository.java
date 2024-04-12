package com.grupo1.infraestructure.repository;

import com.grupo1.infraestructure.entity.TriajeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TriajeRepository extends JpaRepository<TriajeEntity, Long> {
    List<TriajeEntity> findByEstado(Boolean estado);
}
