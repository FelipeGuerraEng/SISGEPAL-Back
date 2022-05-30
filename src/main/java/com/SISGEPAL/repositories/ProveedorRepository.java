package com.SISGEPAL.repositories;

import com.SISGEPAL.entities.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Integer>{

    public ProveedorEntity findByNit(String nit);
    public ProveedorEntity findByCorreo(String email);
    public ProveedorEntity findById(int id);
}
