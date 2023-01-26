package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public class HelloController {

    @GetMapping("/")
    public String sayHello(){
        return "Hello, World!";
    }
}
