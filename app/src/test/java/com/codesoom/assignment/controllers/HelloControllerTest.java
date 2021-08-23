package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HelloController테스트")
class HelloControllerTest {

  @Test
  @DisplayName("Hello World 출력된느지 테스트")
  void testHelloWorld() {
    HelloController helloController = new HelloController();
    assertThat(helloController.hello()).isEqualTo("Hello World!");
  }


}
