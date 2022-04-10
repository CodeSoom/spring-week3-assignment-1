package com.codesoom.assignment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    /**
     * @return 서버 생존 확인용 문자열을 리턴합니다.
     */
    @RequestMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String sayHello() {
        return "Hello, world!";
    }
}
