package com.dux.challenge.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dux.challenge.model.Equipo;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    List<Equipo> findByNombreContainingIgnoreCase(String nombre);
}