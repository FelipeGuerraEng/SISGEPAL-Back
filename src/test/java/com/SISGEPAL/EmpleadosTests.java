package com.SISGEPAL;


import com.SISGEPAL.DTO.empleados.response.EmpleadosDTO;
import com.SISGEPAL.DTO.session.request.UserDTO;
import com.SISGEPAL.DTO.session.response.Session;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.MultiValueMap;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class EmpleadosTests {

    private RestTemplate restTemplate;
    private String url;
    private Logger logger;
    private String token;

    {
        restTemplate = new RestTemplate();
        url = "http://localhost:".concat(String.valueOf(8080));
        logger = Logger.getLogger(this.getClass().toString());
        token = "Bearer ";
    }

    @Test
    void testEmpleado() throws URISyntaxException {
        //PRUEBA EL LOGIN
        final UserDTO userDTO = new UserDTO();
        userDTO.setUsername("mateo.alexander");
        userDTO.setPassword("Tecladoqwerty1!");
        final URI urlFinalLogin = new URI(url.concat("/session"));
        logger.log(Level.SEVERE,urlFinalLogin.toString());
        final RequestEntity<UserDTO> req = new RequestEntity<UserDTO>(userDTO,
                HttpMethod.POST, urlFinalLogin);

        ResponseEntity<Session> res = restTemplate.exchange(req, Session.class);
        final Session session = res.getBody();

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(session).isNotNull();
        assertThat(session.getToken()).isNotNull().isNotEqualTo("");
        this.token = this.token.concat(session.getToken());

        //PRUEBA EL GET DE EMPLEADOS
        final URI urlFinalEmpleado = new URI(url.concat("/empleados"));
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization",this.token);
        logger.log(Level.SEVERE,headers.get("Authorization").toString());
        final RequestEntity<UserDTO> reqEm
                = new RequestEntity<UserDTO>(headers,HttpMethod.GET, urlFinalEmpleado);
        ResponseEntity<EmpleadosDTO> resEm
                = restTemplate.exchange(reqEm, EmpleadosDTO.class);
        final EmpleadosDTO empleadosDTO = resEm.getBody();

        assertThat(resEm.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(empleadosDTO.getEmpleados()).isNotNull();
        assertThat(empleadosDTO.getEmpleados().size()).isGreaterThanOrEqualTo(1);
    }

}
