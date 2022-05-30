package com.SISGEPAL.DTO.proveedores.request;

import com.SISGEPAL.DTO.session.request.UserDTO;
import lombok.Data;

@Data
public class NewProveedorDTO {

    private String nit;
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
}




