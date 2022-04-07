package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController 클래스는 클라이언트와 서버가 제대로 연결되었는지 확인하기 위한 테스트 컨트롤러의 역할을 합니다.<br>
 *
 * 간단하고 빠르게 테스트하기 위해서 Hello, world!가 화면에 잘출력되는지 확인합니다.<br>
 *
 * 잘출력된다면 서버와 클라이언트가 서로 제대로 연결이 되었다는 것을 의미합니다.<br>
 *
 * 제대로 출력이 되지 않는다면 서버 또는 연결에 문제가 있다는 것을 의미합니다.<br>
 */

@RestController
public class HelloController {
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
