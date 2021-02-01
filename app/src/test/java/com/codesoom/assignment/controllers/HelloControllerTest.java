package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HelloControllerTest {
  @Test
  void testSayHello() {
    HelloController helloController = new HelloController();
    //AssertJ
    assertThat(helloController.hello()).isEqualTo("Hello World!");
    //JUnit5
    assertEquals(helloController.hello(), "Hello World!");
  }
}