package com.SISGEPAL.controllers;

import com.SISGEPAL.DTO.session.request.User;
import com.SISGEPAL.DTO.session.response.Session;
import com.SISGEPAL.exceptions.NotFoundException;
import com.SISGEPAL.exceptions.UnauthorizedException;
import com.SISGEPAL.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/session")
public class SessioController {

    @Autowired
    public SessionService sessionService;

    @PostMapping
    public ResponseEntity<Session> getHelloWorld(@RequestBody User user) throws NotFoundException, UnauthorizedException {

        return new ResponseEntity<Session>(sessionService.validateSession(user), HttpStatus.CREATED);
    }


}
