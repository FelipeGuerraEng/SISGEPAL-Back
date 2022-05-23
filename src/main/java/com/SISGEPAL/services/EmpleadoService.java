package com.SISGEPAL.services;

import com.SISGEPAL.entities.Empleado;
import com.SISGEPAL.repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public Empleado findEmpleadoByCedula(String cedula){
        return empleadoRepository.findByCedula(cedula);
    }
}
