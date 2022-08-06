package com.SISGEPAL.DTO.empleados.response;

import lombok.Data;

@Data
public class EmpleadoDTO {
    private int empleado_id;
    private String cedula;
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
}
