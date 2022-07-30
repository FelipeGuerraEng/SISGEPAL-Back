package com.SISGEPAL.DTO.productos.request;
import com.SISGEPAL.DTO.session.request.UserDTO;
import lombok.Data;

@Data
public class NewProductoDTO {
    private String codigo_producto;
    private  int proveedor_id;
    private String nombre;
    private int stock;
    private double precio;
    //private UserDTO userDTO;
}
