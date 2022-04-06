package com.codesoom.assignment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pjkfckr
 * @version 1.0
 */
@RestController
public class HelloController {

    /**
     * @return 성공시 StatusCode(200) 과 문자열을 반환합니다.
     * @apiNote 문자열을 반환하는 API
     */
    @RequestMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String sayHello() {
        return "Hello, world!";
    }
}
