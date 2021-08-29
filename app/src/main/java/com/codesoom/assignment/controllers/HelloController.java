package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 웹 서버의 작동여부를 확인하는 클라이언트의 요청을 처리한다.
 */
@RestController
public class HelloController {
	@RequestMapping("/")
	public String sayHello() {
		return "Hello, world!";
	}
}
