package com.SISGEPAL.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/helloAdmin")
public class HelloWorldAdminController {

    @GetMapping
    public String doHelloWorld(){
        return "Hello world admin";
    }


}
