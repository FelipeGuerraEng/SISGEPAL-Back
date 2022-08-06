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

    public boolean isValidNewLogin(String username) {
        LoginEntity loginEntity = loginRepository.findByUsuario(username);
        return loginEntity == null && !username.trim().equals("");
    }

    public String generatePassword(){
        return RandomString.make(passwordLength);
    }

    public LoginEntity createLogin(String username, EmpleadoEntity empleado)
            throws ConflictException, MessagingException, IOException {

        if(isValidNewLogin((username))) {
            LoginEntity loginEntity = new LoginEntity();
            String password = generatePassword();
            String cipherPassword = passwordEncoder.encode(password);

            loginEntity.setUsuario(username);
            loginEntity.setEmpleado(empleado);
            loginEntity.setPassword(cipherPassword);

            loginEntity = loginRepository.save(loginEntity);
            mailingService.sendCredentialEmail(empleado.getCorreo()
                    ,empleado.getNombre(),loginEntity.getUsuario(), password);
            return loginEntity;
        }

        throw new ConflictException(String.format("Ya existe un usuario %s",
                username));
    }
}
