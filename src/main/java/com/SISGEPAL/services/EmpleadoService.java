package com.SISGEPAL.services;

import com.SISGEPAL.DTO.empleados.request.NewEmpleadoDTO;
import com.SISGEPAL.DTO.empleados.response.EmpleadoDTO;
import com.SISGEPAL.DTO.empleados.response.EmpleadosDTO;
import com.SISGEPAL.entities.EmpleadoEntity;
import com.SISGEPAL.exceptions.BadRequestException;
import com.SISGEPAL.repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    public EmpleadoEntity createEmpleado(NewEmpleadoDTO empleadoDTO) throws BadRequestException {
        final String correo = empleadoDTO.getCorreo();
        final boolean isValidEmailFormat = isValidEmailFormat(correo);
        final boolean isRepeatedEmail = isRepeatedEmail(correo) == 1;

        if(isValidEmailFormat && !isRepeatedEmail) {
            EmpleadoEntity empleado = new EmpleadoEntity();

            empleado.setCedula(empleadoDTO.getCedula());
            empleado.setCorreo(empleadoDTO.getCorreo());
            empleado.setDireccion(empleadoDTO.getDireccion());
            empleado.setNombre(empleadoDTO.getNombre());
            empleado.setTelefono(empleadoDTO.getTelefono());

            empleado = empleadoRepository.save(empleado);

            return empleado;
        }
        String message = "";

        if(!isValidEmailFormat){
            message = String.format("Correo no válido: %s",correo);
        }else{
            if(isRepeatedEmail) {
                message = String.format("Correo en uso: %S",correo);

            }
        }

        throw new BadRequestException(message);
    }

    public boolean isValidEmailFormat(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(email).matches();
    }

    public int isRepeatedEmail(String email) {
        return empleadoRepository.findByCorreo(email) != null ?
                1 : 0;
    }
}
