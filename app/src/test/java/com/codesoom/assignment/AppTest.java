package com.codesoom.assignment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

	private App app;

	@BeforeEach
	void setUp() {
		app = new App();
	}

	@Test
	void getGreeting() {
		Assertions.assertThat(app.getGreeting()).isEqualTo("Hello, world!");
	}

}