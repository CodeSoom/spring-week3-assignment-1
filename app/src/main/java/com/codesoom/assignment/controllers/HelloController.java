package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * "/" http request를 처리한다.
 */
@RestController
public class HelloController {
	/**
	 * "/" http request 요청시, "Hello, world!" 문자열을 리턴한다.
	 *
	 * @return "Hello, world!" 문자열
	 */
	@RequestMapping("/")
	public String sayHello() {
		return "Hello, world!";
	}
}
