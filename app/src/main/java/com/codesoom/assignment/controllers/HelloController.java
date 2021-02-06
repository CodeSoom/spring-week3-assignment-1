package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public Long healthCheck() {
        return System.currentTimeMillis();
    }

}
