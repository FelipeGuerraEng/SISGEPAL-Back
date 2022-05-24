package com.SISGEPAL.DTO.empleados.response;

import lombok.Data;

import java.util.List;

@Data
public class EmpleadosDTO {
    private List<EmpleadoDTO> empleados;
}
