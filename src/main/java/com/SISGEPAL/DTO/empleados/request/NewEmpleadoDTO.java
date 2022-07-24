package com.SISGEPAL.DTO.empleados.request;

import com.SISGEPAL.DTO.session.request.UserDTO;
import lombok.Data;

@Data
public class NewEmpleadoDTO {
    private String cedula;
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
    private String username;
}
