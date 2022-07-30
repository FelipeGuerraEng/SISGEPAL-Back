package com.SISGEPAL.DTO.clientes.response;

import lombok.Data;


@Data
public class ClienteDTO {

    private int cliente_id;
    private String cedula;
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
}