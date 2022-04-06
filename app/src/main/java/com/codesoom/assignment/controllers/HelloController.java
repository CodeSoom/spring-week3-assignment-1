package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Todo 서버가 작동하는지 확인할 수 있는 메세지를 반환합니다.
 */
@RestController
public class HelloController {
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
