package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 프로젝트 설정이 정상적으로 완료되었는지 확인하기 위한 클래스입니다.
 */
@RestController
public class HelloController {

    /**
     * 프로젝트 초기 설정 확인을 위함이며, 루트 컨텍스트로 요청을 보내면 임의의 문자열을 반환한다.
     * @return 문자열
     */
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
