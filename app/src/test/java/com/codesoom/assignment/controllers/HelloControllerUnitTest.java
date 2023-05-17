package com.codesoom.assignment.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelloControllerUnitTest {

	private HelloController helloController;

	@BeforeEach
	void setUp() {
		helloController = new HelloController();
	}

	@Test
	void helloWithNotName() {
		Assertions.assertThat(helloController.hello()).isEqualTo("Hello, world!");
	}

}