package com.SISGEPAL.services;

import com.SISGEPAL.DTO.session.request.UserDTO;
import com.SISGEPAL.entities.EmpleadoEntity;
import com.SISGEPAL.entities.LoginEntity;
import com.SISGEPAL.exceptions.ConflictException;
import com.SISGEPAL.repositories.LoginRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
public class LoginService {

    @Value("${account.password.length}")
    private int passwordLength;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private MailingService mailingService;

    public boolean isValidNewLogin(UserDTO user) {
        LoginEntity loginEntity = loginRepository.findByUsuario(user.getUsername());
        return loginEntity == null && !user.getPassword().trim().equals("");
    }

    public String generatePassword(){
        return RandomString.make(passwordLength);
    }

    public LoginEntity createLogin(UserDTO userDTO, EmpleadoEntity empleado)
            throws ConflictException, MessagingException, IOException {

        if(isValidNewLogin((userDTO))) {
            LoginEntity loginEntity = new LoginEntity();
            String cipherPassword = passwordEncoder.encode(userDTO.getPassword());

            loginEntity.setUsuario(userDTO.getUsername());
            loginEntity.setEmpleado(empleado);
            loginEntity.setPassword(cipherPassword);

            loginEntity = loginRepository.save(loginEntity);
            mailingService.sendCredentialEmail(empleado.getCorreo()
                    ,empleado.getNombre(),loginEntity.getUsuario(), userDTO.getPassword());
            return loginEntity;
        }

        throw new ConflictException(String.format("Ya existe un usuario %s",
                userDTO.getUsername()));
    }
}
