package com.SISGEPAL.DTO.clientes.request;

import lombok.Data;

@Data
public class UpdateClienteDTO {

    private String cedula;
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
}