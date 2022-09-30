package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/healthcheck")
    public String checkServerHealth() {
        return "Server is OK";
    }
}
