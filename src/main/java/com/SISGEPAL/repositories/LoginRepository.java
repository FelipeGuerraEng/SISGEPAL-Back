package com.SISGEPAL.repositories;

import com.SISGEPAL.entities.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Integer> {

    public Login findByUsuario(String user);
}