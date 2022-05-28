package com.SISGEPAL.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "administradores")
@Data
public class AdministradorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "administrador_id", nullable = false)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "empleado_id", unique = true)
    private EmpleadoEntity empleado;
}