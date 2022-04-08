package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController 클래스는 클라이언트와 서버가 제대로 연결되었는지 확인하기 위한 테스트 컨트롤러의 역할을 합니다.<br>
 */

@RestController
public class HelloController {
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
