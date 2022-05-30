package com.SISGEPAL.services;



import com.SISGEPAL.DTO.proveedores.request.NewProveedorDTO;
import com.SISGEPAL.DTO.proveedores.request.UpdateProveedorDTO;
import com.SISGEPAL.DTO.proveedores.response.ProveedorDTO;
import com.SISGEPAL.DTO.proveedores.response.ProveedoresDTO;
import com.SISGEPAL.entities.ProveedorEntity;
import com.SISGEPAL.exceptions.BadRequestException;
import com.SISGEPAL.exceptions.ConflictException;
import com.SISGEPAL.exceptions.NotFoundException;
import com.SISGEPAL.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private LoginService loginService;
    @Autowired
    private MailingService mailingService;
    @Autowired
    private AdministradorService administradorService;

    public ProveedorEntity findProveedorByNit(String nit){
        return proveedorRepository.findByNit(nit);
    }

    public List<ProveedorEntity> findAll(){
        return proveedorRepository.findAll();
    }

    public ProveedoresDTO mapToProveedoresDTO(List<ProveedorEntity> proveedores){
        List<ProveedorDTO> proveedoresDTOList =
                new ArrayList<ProveedorDTO>();
        ProveedoresDTO proveedoresDTO = new ProveedoresDTO();

        proveedores.forEach(e -> {
            ProveedorDTO proveedorDTO = mapToProveedorDTO(e);
            proveedoresDTOList.add(proveedorDTO);
        });

        proveedoresDTO.setProveedores(proveedoresDTOList);
        return proveedoresDTO;
    }

    public ProveedorDTO mapToProveedorDTO(ProveedorEntity proveedor){
        ProveedorDTO proveedorDTO = new ProveedorDTO();

        proveedorDTO.setProveedor_id(proveedor.getId());
        proveedorDTO.setNit(proveedor.getNit());
        proveedorDTO.setNombre(proveedor.getNombre());
        proveedorDTO.setCorreo(proveedor.getCorreo());
        proveedorDTO.setDireccion(proveedor.getDireccion());
        proveedorDTO.setTelefono(proveedor.getTelefono());

        return proveedorDTO;
    }

    public ProveedorEntity createProveedor (NewProveedorDTO proveedorDTO) throws BadRequestException, ConflictException, MessagingException, IOException {
        final String correo = proveedorDTO.getCorreo();
        final boolean isValidEmailFormat = isValidEmailFormat(correo);
        final boolean isRepeatedEmail = isRepeatedEmail(correo) == 1;
        final int repeatedNit = isRepeatedNit(proveedorDTO.getNit());
        final boolean isValidNit = repeatedNit == 0;

        if(isValidEmailFormat && !isRepeatedEmail  && isValidNit) {
            ProveedorEntity proveedor = new ProveedorEntity();

            proveedor.setNit(proveedorDTO.getNit());
            proveedor.setCorreo(proveedorDTO.getCorreo());
            proveedor.setDireccion(proveedorDTO.getDireccion());
            proveedor.setNombre(proveedorDTO.getNombre());
            proveedor.setTelefono(proveedorDTO.getTelefono());

            proveedor = proveedorRepository.save(proveedor);

            return proveedor;
        }
        String message = "";

        if(!isValidEmailFormat){
            message = String.format("Correo no válido: %s",correo);
        }else{
            if(isRepeatedEmail) {
                message = String.format("Correo en uso: %S",correo);

            }

            if(!isValidNit) {
                message = String.format("El NIT ya está en uso");
            }
        }

        throw new BadRequestException(message);
    }

    public ProveedorEntity updateProveedor(UpdateProveedorDTO proveedorDTO, int proveedorID)

                throws NotFoundException, BadRequestException, MessagingException, IOException {
            ProveedorEntity proveedor = proveedorRepository.findById(proveedorID);

            if(proveedor == null) {
                throw new NotFoundException(String.format(
                        "No existe un proveedor con id %d",proveedorID
                ));
            }

            final boolean isValidEmailFormat = isValidEmailFormat(proveedorDTO.getCorreo());
            final int repeatedEmail = isRepeatedEmail(proveedorDTO.getCorreo());
            final boolean isValidEmail = ( repeatedEmail == 1
                    && proveedor.getCorreo().equals(proveedorDTO.getCorreo())) || repeatedEmail == 0;
            final int repeatedNit = isRepeatedNit(proveedorDTO.getNit());
            final boolean isValidNit = ( repeatedNit == 1
                    && proveedor.getNit().equals(proveedorDTO.getNit())) || repeatedNit == 0;

            if(isValidEmailFormat && isValidEmail && isValidNit ) {
                final String oldEmail = proveedor.getCorreo();
                final String newEmail = proveedorDTO.getCorreo();
                proveedor.setNit(proveedorDTO.getNit());
                proveedor.setNombre(proveedorDTO.getNombre());
                proveedor.setCorreo(newEmail);
                proveedor.setDireccion(proveedorDTO.getDireccion());
                proveedor.setTelefono(proveedorDTO.getTelefono());

                proveedorRepository.save(proveedor);

                if(!newEmail.equals(oldEmail)){
                    mailingService.sendUpdatedEmail(newEmail,
                            proveedor.getNombre(), newEmail);
                }

                return proveedor;
            }

            String message = "";

            message = isValidEmailFormat? "":"Correo no válido-";
            message += isValidEmail? "":"-Correo en uso";
            message += isValidNit? "":"-Nit en uso";

            throw  new BadRequestException(message);

        }

    public ProveedorEntity deleteProveedor(int proveedorID, Object principal) throws BadRequestException {

        ProveedorEntity proveedor = proveedorRepository.findById(proveedorID);

        proveedorRepository.delete(proveedor);

        return proveedor;
    }

    public boolean isValidEmailFormat(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(email).matches();
    }

    public int isRepeatedEmail(String email) {
        return proveedorRepository.findByCorreo(email) != null ?
                1 : 0;
    }

    public int isRepeatedNit(String cc) {
        return proveedorRepository.findByNit(cc) != null ?
                1 : 0;
    }


}
