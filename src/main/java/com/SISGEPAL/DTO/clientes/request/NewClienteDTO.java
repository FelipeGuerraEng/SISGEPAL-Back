package com.SISGEPAL.DTO.clientes.request;

import com.SISGEPAL.DTO.session.request.UserDTO;
import lombok.Data;

@Data
public class NewClienteDTO {

    private String cedula;
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
}