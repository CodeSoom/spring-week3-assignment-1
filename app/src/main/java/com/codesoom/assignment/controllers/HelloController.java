package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 웹 서버의 작동여부를 확인하는 클라이언트의 요청을 처리한다.
 */
@RestController
public class HelloController {
	/**
	 * 웹 서버의 작동여부를 확인하는 클라이언트의 요청이 들어온 경우,
	 * 해당 웹 서버가 정상 작동함을 확인시켜주는 메시지를 리턴한다.
	 *
	 * @return 웹 서버가 정상 작동함을 확인시켜주는 메시지
	 */
	@RequestMapping("/")
	public String sayHello() {
		return "Hello, world!";
	}
}
