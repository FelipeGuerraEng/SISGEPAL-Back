package com.SISGEPAL.DTO.productos.response;

import lombok.Data;

@Data
public class ProductoDTO {
private int producto_id;
private String codigo_producto;
private  int proveedor_id;
private String nombre;
private int stock;
private double precio;




}
