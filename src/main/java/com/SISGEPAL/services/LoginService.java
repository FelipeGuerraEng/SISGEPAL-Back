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

@Service
public class LoginService {

    @Value("${account.password.length}")
    private int passwordLength;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginRepository loginRepository;

    public boolean isValidUsername(String username) {
        LoginEntity loginEntity = loginRepository.findByUsuario(username);
        return loginEntity == null;
    }

    public String generatePassword(){
        return RandomString.make(passwordLength);
    }

    public LoginEntity createLogin(UserDTO userDTO, EmpleadoEntity empleado) throws ConflictException {

        if(isValidUsername((userDTO.getUsername()))) {
            LoginEntity loginEntity = new LoginEntity();
            String generatedPass = generatePassword();
            String cipherPassword = passwordEncoder.encode(generatedPass);

            loginEntity.setUsuario(userDTO.getUsername());
            loginEntity.setEmpleado(empleado);
            loginEntity.setPassword(cipherPassword);

            loginEntity = loginRepository.save(loginEntity);

            return loginEntity;
        }

        throw new ConflictException(String.format("Ya existe un usuario %s",
                userDTO.getUsername()));
    }
}
