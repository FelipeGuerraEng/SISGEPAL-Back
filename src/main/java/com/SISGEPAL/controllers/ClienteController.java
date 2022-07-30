package com.SISGEPAL.controllers;


import com.SISGEPAL.DTO.clientes.request.NewClienteDTO;
import com.SISGEPAL.DTO.clientes.request.UpdateClienteDTO;
import com.SISGEPAL.DTO.clientes.response.ClienteDTO;
import com.SISGEPAL.DTO.clientes.response.ClientesDTO;
import com.SISGEPAL.exceptions.BadRequestException;
import com.SISGEPAL.exceptions.ConflictException;
import com.SISGEPAL.exceptions.NotFoundException;
import com.SISGEPAL.services.EmpleadoService;
import com.SISGEPAL.services.MailingService;
import com.SISGEPAL.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/clientes")
@CrossOrigin("*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MailingService mailingService;


    @GetMapping
    public ResponseEntity<ClientesDTO> getClientes(){
        ClientesDTO clientesDTO =
                clienteService.mapToClientesDTO(
                        clienteService.findAll()
                );
        ResponseEntity<ClientesDTO> response
                = new ResponseEntity<ClientesDTO>(clientesDTO, HttpStatus.OK);

        return response;
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> postCliente(@RequestBody NewClienteDTO newClienteDTO)
            throws BadRequestException, ConflictException, MessagingException, IOException {
        ClienteDTO clienteDTO =
                clienteService.mapToClienteDTO(clienteService.createCliente(newClienteDTO));
        ResponseEntity<ClienteDTO> response
                = new ResponseEntity<ClienteDTO>(clienteDTO, HttpStatus.CREATED);
        return response;

    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> putCliente(@RequestBody UpdateClienteDTO updateClienteDTO,
                                                     @PathVariable int id)
            throws BadRequestException, ConflictException, MessagingException, IOException, NotFoundException {
        ClienteDTO clienteDTO =
                clienteService.mapToClienteDTO(clienteService.updateCliente(updateClienteDTO, id));
        ResponseEntity<ClienteDTO> response
                = new ResponseEntity<ClienteDTO>(clienteDTO, HttpStatus.OK);
        return response;

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClienteDTO> deleteCliente(Authentication authentication, @PathVariable int id)
            throws BadRequestException {
        ClienteDTO clienteDTO =
                clienteService.mapToClienteDTO(clienteService.deleteCliente(id, authentication.getPrincipal()));
        ResponseEntity<ClienteDTO> response
                = new ResponseEntity<ClienteDTO>(clienteDTO, HttpStatus.OK);
        return response;

    }


}