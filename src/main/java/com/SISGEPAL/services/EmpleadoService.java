package com.SISGEPAL.services;

import com.SISGEPAL.DTO.empleados.request.NewEmpleadoDTO;
import com.SISGEPAL.DTO.empleados.request.UpdateEmpleadoDTO;
import com.SISGEPAL.DTO.empleados.response.EmpleadoDTO;
import com.SISGEPAL.DTO.empleados.response.EmpleadosDTO;
import com.SISGEPAL.entities.EmpleadoEntity;
import com.SISGEPAL.exceptions.BadRequestException;
import com.SISGEPAL.exceptions.ConflictException;
import com.SISGEPAL.exceptions.NotFoundException;
import com.SISGEPAL.repositories.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private LoginService loginService;
    @Autowired
    private MailingService mailingService;
    @Autowired
    private AdministradorService administradorService;

    public EmpleadoEntity findEmpleadoByCedula(String cedula){
        return empleadoRepository.findByCedula(cedula);
    }

    public List<EmpleadoEntity> findAll(){
        return empleadoRepository.findAll();
    }

    public EmpleadosDTO mapToEmpleadosDTo(List<EmpleadoEntity> empleados){
        List<EmpleadoDTO> empleadosDTOList =
                new ArrayList<EmpleadoDTO>();
        EmpleadosDTO empleadosDTO = new EmpleadosDTO();

        empleados.forEach(e -> {
            EmpleadoDTO empleadoDTO = mapToEmpleadoDTO(e);
            empleadosDTOList.add(empleadoDTO);
        });

        empleadosDTO.setEmpleados(empleadosDTOList);
        return empleadosDTO;
    }

    public EmpleadoDTO mapToEmpleadoDTO(EmpleadoEntity empleado){
        EmpleadoDTO empleadoDTO = new EmpleadoDTO();

        empleadoDTO.setEmpleado_id(empleado.getId());
        empleadoDTO.setCedula(empleado.getCedula());
        empleadoDTO.setNombre(empleado.getNombre());
        empleadoDTO.setCorreo(empleado.getCorreo());
        empleadoDTO.setDireccion(empleado.getDireccion());
        empleadoDTO.setTelefono(empleado.getTelefono());

        return empleadoDTO;
    }

    public EmpleadoEntity createEmpleado(NewEmpleadoDTO empleadoDTO) throws BadRequestException, ConflictException, MessagingException, IOException {
        final String correo = empleadoDTO.getCorreo();
        final boolean isValidEmailFormat = isValidEmailFormat(correo);
        final boolean isRepeatedEmail = isRepeatedEmail(correo) == 1;
        final boolean isValidLogin = loginService.isValidNewLogin(empleadoDTO.getUsername());
        final int repeatedCC = isRepeatedCC(empleadoDTO.getCedula());
        final boolean isValidCC = repeatedCC == 0;

        if(isValidEmailFormat && !isRepeatedEmail
                && isValidLogin && isValidCC) {
            EmpleadoEntity empleado = new EmpleadoEntity();

            empleado.setCedula(empleadoDTO.getCedula());
            empleado.setCorreo(empleadoDTO.getCorreo());
            empleado.setDireccion(empleadoDTO.getDireccion());
            empleado.setNombre(empleadoDTO.getNombre());
            empleado.setTelefono(empleadoDTO.getTelefono());

            empleado = empleadoRepository.save(empleado);
            loginService.createLogin(empleadoDTO.getUsername(), empleado);

            return empleado;
        }
        String message = "";

        if(!isValidEmailFormat){
            message = String.format("Correo no válido: %s",correo);
        }else{
            if(isRepeatedEmail) {
                message = String.format("Correo en uso: %S",correo);

            }

            if(!isValidLogin) {
                message = String.format("Usuario o contraseña no válidos");
            }

            if(!isValidCC) {
                message = String.format("La cédula ya está en uso");
            }
        }

        throw new BadRequestException(message);
    }

    public EmpleadoEntity updateEmpleado(UpdateEmpleadoDTO empleadoDTO, int empleadoID)
            throws NotFoundException, BadRequestException, MessagingException, IOException {
        EmpleadoEntity empleado = empleadoRepository.findById(empleadoID);

        if(empleado == null) {
            throw new NotFoundException(String.format(
                    "No existe un empleado con id %d", empleadoID
            ));
        }

        final boolean isValidEmailFormat = isValidEmailFormat(empleadoDTO.getCorreo());
        final int repeatedEmail = isRepeatedEmail(empleadoDTO.getCorreo());
        final boolean isValidEmail = ( repeatedEmail == 1
                && empleado.getCorreo().equals(empleadoDTO.getCorreo())) || repeatedEmail == 0;
        final int repeatedCC = isRepeatedCC(empleadoDTO.getCedula());
        final boolean isValidCC = ( repeatedCC == 1
                && empleado.getCedula().equals(empleadoDTO.getCedula())) || repeatedCC == 0;

        if(isValidEmailFormat && isValidEmail && isValidCC ) {
            final String oldEmail = empleado.getCorreo();
            final String newEmail = empleadoDTO.getCorreo();
            empleado.setCedula(empleadoDTO.getCedula());
            empleado.setNombre(empleadoDTO.getNombre());
            empleado.setCorreo(newEmail);
            empleado.setDireccion(empleadoDTO.getDireccion());
            empleado.setTelefono(empleadoDTO.getTelefono());

            empleadoRepository.save(empleado);

            if(!newEmail.equals(oldEmail)){
                mailingService.sendUpdatedEmail(newEmail,
                        empleado.getNombre(), newEmail);
            }

            return empleado;
        }

        String message = "";

        message = isValidEmailFormat? "":"Correo no válido-";
        message += isValidEmail? "":"-Correo en uso";
        message += isValidCC? "":"-Cédula en uso";

        throw  new BadRequestException(message);

    }

    public EmpleadoEntity deleteEmpleado(int empleadoID, Object principal) throws BadRequestException {
        EmpleadoEntity empleado = empleadoRepository.findById(empleadoID);
        final int numAdmin = administradorService.howManyAdministradfores();
        final boolean isAdmin = administradorService.isAdministrador(empleado);
        final String ccAuthentication = (String) principal;
        if(numAdmin == 1 && isAdmin){
            throw new BadRequestException("No es posible eliminar el único administrador que existe");
        }
        if(ccAuthentication.equals(empleado.getCedula())){
            throw new BadRequestException("No es posible eliminar un usuario que se encuentra en sesión");
        }


        empleadoRepository.delete(empleado);

        return empleado;
    }

    public boolean isValidEmailFormat(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(email).matches();
    }

    public int isRepeatedEmail(String email) {
        return empleadoRepository.findByCorreo(email) != null ?
                1 : 0;
    }

    public int isRepeatedCC(String cc) {
        return empleadoRepository.findByCedula(cc) != null ?
                1 : 0;
    }
}
