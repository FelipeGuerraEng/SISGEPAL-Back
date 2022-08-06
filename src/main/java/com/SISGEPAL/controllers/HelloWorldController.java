package com.SISGEPAL.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {

    @GetMapping
    public String doHelloWorld(){
        return "Hello world";
    }


}
