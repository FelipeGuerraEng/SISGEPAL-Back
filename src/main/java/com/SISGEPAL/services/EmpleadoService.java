package com.SISGEPAL.services;

import com.SISGEPAL.DTO.empleados.response.EmpleadoDTO;
import com.SISGEPAL.DTO.empleados.response.EmpleadosDTO;
import com.SISGEPAL.entities.EmpleadoEntity;
import com.SISGEPAL.repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public EmpleadoEntity findEmpleadoByCedula(String cedula){
        return empleadoRepository.findByCedula(cedula);
    }

    public List<EmpleadoEntity> findAll(){
        return empleadoRepository.findAll();
    }

    public EmpleadosDTO mapToEmpleadosDTo(List<EmpleadoEntity> empleados){
        List<EmpleadoDTO> empleadosDTOList =
                new ArrayList<EmpleadoDTO>();
        EmpleadosDTO empleadosDTO = new EmpleadosDTO();

        empleados.forEach(e -> {
            EmpleadoDTO empleadoDTO = mapToEmpleadoDTO(e);
            empleadosDTOList.add(empleadoDTO);
        });

        empleadosDTO.setEmpleados(empleadosDTOList);
        return empleadosDTO;
    }

    public EmpleadoDTO mapToEmpleadoDTO(EmpleadoEntity empleado){
        EmpleadoDTO empleadoDTO = new EmpleadoDTO();

        empleadoDTO.setEmpleado_id(empleado.getId());
        empleadoDTO.setCedula(empleado.getCedula());
        empleadoDTO.setNombre(empleado.getNombre());
        empleadoDTO.setCorreo(empleado.getCorreo());
        empleadoDTO.setDireccion(empleado.getDireccion());
        empleadoDTO.setTelefono(empleado.getTelefono());

        return empleadoDTO;
    }
}
