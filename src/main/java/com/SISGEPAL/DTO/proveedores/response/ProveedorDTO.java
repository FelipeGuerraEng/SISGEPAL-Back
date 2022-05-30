package com.SISGEPAL.DTO.proveedores.response;

import lombok.Data;


@Data
public class ProveedorDTO {

    private int proveedor_id;
    private String nit;
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
}
