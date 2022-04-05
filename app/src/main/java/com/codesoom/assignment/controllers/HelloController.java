package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 콘솔창에서 클라이언트가 서버에 메세지를 보낼 때<br>
 * 아무 메세지도 보내지 않고, 주소(localhost:8080)만 보내는 경우에<br>
 * 클라이언트에 대한 서버의 응답을 처리해주는 클래스입니다.
 */

@RestController
public class HelloController {
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
