package com.SISGEPAL.controllers;


import com.SISGEPAL.DTO.proveedores.request.NewProveedorDTO;
import com.SISGEPAL.DTO.proveedores.request.UpdateProveedorDTO;
import com.SISGEPAL.DTO.proveedores.response.ProveedorDTO;
import com.SISGEPAL.DTO.proveedores.response.ProveedoresDTO;
import com.SISGEPAL.exceptions.BadRequestException;
import com.SISGEPAL.exceptions.ConflictException;
import com.SISGEPAL.exceptions.NotFoundException;
import com.SISGEPAL.services.EmpleadoService;
import com.SISGEPAL.services.MailingService;
import com.SISGEPAL.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/proveedores")
@CrossOrigin("*")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private MailingService mailingService;


    @GetMapping
    public ResponseEntity<ProveedoresDTO> getProveedores(){
        ProveedoresDTO proveedoresDTO =
                proveedorService.mapToProveedoresDTO(
                        proveedorService.findAll()
                );
        ResponseEntity<ProveedoresDTO> response
                = new ResponseEntity<ProveedoresDTO>(proveedoresDTO, HttpStatus.OK);

        return response;
    }

    @PostMapping
    public ResponseEntity<ProveedorDTO> postProveedor(@RequestBody NewProveedorDTO newProveedorDTO)
            throws BadRequestException, ConflictException, MessagingException, IOException {
        ProveedorDTO proveedorDTO =
                proveedorService.mapToProveedorDTO(proveedorService.createProveedor(newProveedorDTO));
        ResponseEntity<ProveedorDTO> response
                = new ResponseEntity<ProveedorDTO>(proveedorDTO,HttpStatus.CREATED);
        return response;

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> putProveedor(@RequestBody UpdateProveedorDTO updateProveedorDTO,
                                                   @PathVariable int id)
            throws BadRequestException, ConflictException, MessagingException, IOException, NotFoundException {
        ProveedorDTO proveedorDTO =
                proveedorService.mapToProveedorDTO(proveedorService.updateProveedor(updateProveedorDTO,id));
        ResponseEntity<ProveedorDTO> response
                = new ResponseEntity<ProveedorDTO>(proveedorDTO,HttpStatus.OK);
        return response;

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProveedorDTO> deleteProveedor(Authentication authentication, @PathVariable int id)
            throws BadRequestException {
        ProveedorDTO proveedorDTO =
                proveedorService.mapToProveedorDTO(proveedorService.deleteProveedor(id, authentication.getPrincipal()));
        ResponseEntity<ProveedorDTO> response
                = new ResponseEntity<ProveedorDTO>(proveedorDTO,HttpStatus.OK);
        return response;

    }


}
