package com.SISGEPAL.repositories;

import com.SISGEPAL.entities.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginEntity, Integer> {

    public LoginEntity findByUsuario(String user);
}