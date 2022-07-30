package com.SISGEPAL.services;

import com.SISGEPAL.DTO.clientes.request.NewClienteDTO;
import com.SISGEPAL.DTO.clientes.request.UpdateClienteDTO;
import com.SISGEPAL.DTO.clientes.response.ClienteDTO;
import com.SISGEPAL.DTO.clientes.response.ClientesDTO;
import com.SISGEPAL.entities.ClienteEntity;
import com.SISGEPAL.exceptions.BadRequestException;
import com.SISGEPAL.exceptions.ConflictException;
import com.SISGEPAL.exceptions.NotFoundException;
import com.SISGEPAL.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private LoginService loginService;
    @Autowired
    private AdministradorService administradorService;

    public ClienteEntity findClienteByCedula(String cedula){

        return clienteRepository.findByCedula(cedula);

    }

    public List<ClienteEntity> findAll(){

        return clienteRepository.findAll();

    }

    public ClientesDTO mapToClientesDTO(List<ClienteEntity> clientes){
        List<ClienteDTO> clientesDTOList =
                new ArrayList<ClienteDTO>();
        ClientesDTO clientesDTO = new ClientesDTO();

        clientes.forEach(e -> {
            ClienteDTO clienteDTO = mapToClienteDTO(e);
            clientesDTOList.add(clienteDTO);
        });

        clientesDTO.setClientes(clientesDTOList);
        return clientesDTO;
    }

    public ClienteDTO mapToClienteDTO(ClienteEntity cliente){
        ClienteDTO clienteDTO = new ClienteDTO();

        clienteDTO.setCliente_id(cliente.getId());
        clienteDTO.setCedula(cliente.getCedula());
        clienteDTO.setNombre(cliente.getNombre());
        clienteDTO.setCorreo(cliente.getCorreo());
        clienteDTO.setDireccion(cliente.getDireccion());
        clienteDTO.setTelefono(cliente.getTelefono());

        return clienteDTO;
    }

    public ClienteEntity createCliente (NewClienteDTO clienteDTO) throws BadRequestException, ConflictException, MessagingException, IOException {
        final String correo = clienteDTO.getCorreo();
        final boolean isValidEmailFormat = isValidEmailFormat(correo);
        final boolean isRepeatedEmail = isRepeatedEmail(correo) == 1;
        final int repeatedCedula = isRepeatedCedula(clienteDTO.getCedula());
        final boolean isValidCedula = repeatedCedula == 0;

        if(isValidEmailFormat && !isRepeatedEmail  && isValidCedula) {
            ClienteEntity cliente = new ClienteEntity();

            cliente.setCedula(clienteDTO.getCedula());
            cliente.setCorreo(clienteDTO.getCorreo());
            cliente.setDireccion(clienteDTO.getDireccion());
            cliente.setNombre(clienteDTO.getNombre());
            cliente.setTelefono(clienteDTO.getTelefono());

            cliente = clienteRepository.save(cliente);

            return cliente;
        }
        String message = "";

        if(!isValidEmailFormat){
            message = String.format("Correo no válido: %s",correo);
        }else{
            if(isRepeatedEmail) {
                message = String.format("Correo en uso: %S",correo);

            }

            if(!isValidCedula) {
                message = String.format("La CC ya está en uso");
            }
        }

        throw new BadRequestException(message);
    }

    public ClienteEntity updateCliente(UpdateClienteDTO clienteDTO, int clienteID)

            throws NotFoundException, BadRequestException, MessagingException, IOException {
        ClienteEntity cliente = clienteRepository.findById(clienteID);

        if(cliente == null) {
            throw new NotFoundException(String.format(
                    "No existe un cliente con id %d",clienteID
            ));
        }

        final boolean isValidEmailFormat = isValidEmailFormat(clienteDTO.getCorreo());
        final int repeatedEmail = isRepeatedEmail(clienteDTO.getCorreo());
        final boolean isValidEmail = ( repeatedEmail == 1
                && cliente.getCorreo().equals(clienteDTO.getCorreo())) || repeatedEmail == 0;
        final int repeatedCedula = isRepeatedCedula(clienteDTO.getCedula());
        final boolean isValidCedula = ( repeatedCedula == 1
                && cliente.getCedula().equals(clienteDTO.getCedula())) || repeatedCedula == 0;

        if(isValidEmailFormat && isValidEmail && isValidCedula ) {
            final String oldEmail = cliente.getCorreo();
            final String newEmail = clienteDTO.getCorreo();
            cliente.setCedula(clienteDTO.getCedula());
            cliente.setNombre(clienteDTO.getNombre());
            cliente.setCorreo(newEmail);
            cliente.setDireccion(clienteDTO.getDireccion());
            cliente.setTelefono(clienteDTO.getTelefono());

            clienteRepository.save(cliente);

            return cliente;
        }

        String message = "";

        message = isValidEmailFormat? "":"Correo no válido-";
        message += isValidEmail? "":"-Correo en uso";
        message += isValidCedula? "":"-Cedula en uso";

        throw  new BadRequestException(message);

    }

    public ClienteEntity deleteCliente(int clienteID, Object principal) throws BadRequestException {

        ClienteEntity cliente = clienteRepository.findById(clienteID);

        clienteRepository.delete(cliente);

        return cliente;
    }

    public boolean isValidEmailFormat(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(email).matches();
    }

    public int isRepeatedEmail(String email) {
        return clienteRepository.findByCorreo(email) != null ?
                1 : 0;
    }

    public int isRepeatedCedula(String cc) {
        return clienteRepository.findByCedula(cc) != null ?
                1 : 0;
    }


}