package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 특정 URI로 들어온 요청을 맞는 메서드를 찾아 실행하고, 결과 값이 있으면 바디에 담아 반환한다.
 */
@RestController
public class HelloController {

    /**
     * 해당 URI로 요청이 들어오면, 문자열을 바디에 담아 반환한다.
     * @return 문자열
     */
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
