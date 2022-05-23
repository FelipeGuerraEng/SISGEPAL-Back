package com.SISGEPAL.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "empleados")
@Data
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empleado_id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "cedula")
    private String cedula;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "direccion", length = 100)
    private String direccion;

    @Lob
    @Column(name = "telefono")
    private String telefono;
}