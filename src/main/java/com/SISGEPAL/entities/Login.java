package com.SISGEPAL.entities;

import com.SISGEPAL.entities.Empleado;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "login")
@Data
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", unique = true)
    private Empleado empleado;

    @Column(name = "usuario", length = 15, unique = true)
    private String usuario;

    @Column(name = "password", length = 60)
    private String password;
}