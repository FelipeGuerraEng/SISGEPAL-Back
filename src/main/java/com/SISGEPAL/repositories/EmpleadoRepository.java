package com.SISGEPAL.repositories;

import com.SISGEPAL.entities.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Integer> {
    public EmpleadoEntity findByCedula(String cedula);
    public EmpleadoEntity findByCorreo(String email);
}