package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HelloController테스트")
class HelloControllerTest {

  /**
   * hello 메서드가 "Hello World!"를 출력하는지 테스트
   */
  @Test
  @DisplayName("Hello World 출력 테스트")
  void testHelloWorld() {
    HelloController helloController = new HelloController();
    assertThat(helloController.hello()).isEqualTo("Hello World!");

  }
}
