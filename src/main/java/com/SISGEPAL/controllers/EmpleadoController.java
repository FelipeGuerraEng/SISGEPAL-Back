package com.SISGEPAL.controllers;


import com.SISGEPAL.DTO.empleados.response.EmpleadosDTO;
import com.SISGEPAL.services.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {
    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<EmpleadosDTO> getEmpleados(){
        EmpleadosDTO empleadosDTO =
                empleadoService.mapToEmpleadosDTo(
                        empleadoService.findAll()
                );
        ResponseEntity<EmpleadosDTO> response
                = new ResponseEntity<EmpleadosDTO>(empleadosDTO, HttpStatus.OK);

        return response;
    }
}
