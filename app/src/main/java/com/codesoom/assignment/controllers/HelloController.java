package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 클라이언트와 서버가 제대로 연결되었는지 확인하는 역할을 합니다.
 */

@RestController
public class HelloController {
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
