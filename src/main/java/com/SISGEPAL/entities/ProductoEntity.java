package com.SISGEPAL.entities;
import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "productos")
@Data
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "codigo_producto",length =20, unique = true)
    private String codigoProducto;

    @Column(name = "proveedor_id", unique = true)
    private int proveedor;


    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "stock",nullable = false)
    private int stock;

    @Column(name = "precio", nullable = false )
    private double precio;
}
