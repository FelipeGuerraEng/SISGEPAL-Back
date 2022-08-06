package com.SISGEPAL.repositories;

import com.SISGEPAL.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer>{

    public ClienteEntity findByCedula(String cedula);
    public ClienteEntity findByCorreo(String email);
    public ClienteEntity findById(int id);
}