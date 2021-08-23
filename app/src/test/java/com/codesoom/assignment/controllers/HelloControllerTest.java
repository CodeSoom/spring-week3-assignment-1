package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class HelloControllerTest {

  @Test
  void testHelloWorld() {
    HelloController helloController = new HelloController();
    assertThat(helloController.hello()).isEqualTo("Hello World!");
  }
}
