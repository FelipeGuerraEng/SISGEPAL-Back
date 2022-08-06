package com.SISGEPAL.DTO.productos.request;
import lombok.Data;

@Data
public class UpdateProductoDTO {
    private String codigo_producto;
    private  int proveedor_id;
    private String nombre;
    private int stock;
    private double precio;
}
