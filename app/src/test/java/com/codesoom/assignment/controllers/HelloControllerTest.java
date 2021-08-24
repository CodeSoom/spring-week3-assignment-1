package com.codesoom.assignment.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HelloController sayHello()")
public final class HelloControllerTest {
	@Test
	@DisplayName("should exist")
	void existTest() {
		HelloController helloController = new HelloController();
		assertNotNull(helloController.sayHello(), "HelloController should exist sayHello()");
	}

	@Test
	@DisplayName("should return \"Hello, world!\"")
	void returnTest() {
		HelloController helloController = new HelloController();
		String result = helloController.sayHello();
		assertEquals("Hello, world!", result);
	}
}
