package com.SISGEPAL.repositories;

import com.SISGEPAL.entities.AdministradorEntity;
import com.SISGEPAL.entities.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<AdministradorEntity, Integer> {
    public AdministradorEntity findByEmpleado(EmpleadoEntity empleado);
    @Query(value = "SELECT COUNT(*) FROM ADMINISTRADORES",
        nativeQuery = true)
    public int findHowManyAdministradores();
}