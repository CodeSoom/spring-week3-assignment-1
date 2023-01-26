package com.codesoom.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppTest {

  private App app;

  @BeforeEach
  void setUp() {
    app = new App();
  }

  @Test
  void appHasGreeting() {
    assertThat(app.getGreeting()).isEqualTo("Hello, world!");
  }

  @Test
  void contextLoads() {
  }

  @Test
  void applicationStarts() {
    App.main(new String[]{});
  }
}