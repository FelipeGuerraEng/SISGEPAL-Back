package com.SISGEPAL.DTO.proveedores.request;

import lombok.Data;

@Data
public class UpdateProveedorDTO {

    private String nit;
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
}
