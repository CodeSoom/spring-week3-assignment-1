package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버가 작동하는지 확인하는 역할을 합니다.
 */
@RestController
public class HelloController {
    /**
     * 서버가 작동하는지 확인하는 메세지를 반환합니다.
     * @return 인사 메세지
     */
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
