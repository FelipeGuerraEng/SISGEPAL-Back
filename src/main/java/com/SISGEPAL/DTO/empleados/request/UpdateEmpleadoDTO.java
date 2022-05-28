package com.SISGEPAL.DTO.empleados.request;

import lombok.Data;

@Data
public class UpdateEmpleadoDTO {
    private String cedula;
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
}
