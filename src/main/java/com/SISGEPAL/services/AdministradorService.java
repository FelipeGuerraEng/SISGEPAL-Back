package com.SISGEPAL.services;

import com.SISGEPAL.entities.EmpleadoEntity;
import com.SISGEPAL.repositories.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {

    @Autowired
    public AdministradorRepository administradorRepository;

    public boolean isAdministrador(EmpleadoEntity empleado){
        return administradorRepository.findByEmpleado(empleado) != null;
    }
}
