package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("App 클래스")
class AppTest {
    @Test
    @DisplayName("getGreeting test")
    void app_GetGreeting() {
        App app = new App();
        String greeting = app.getGreeting();
        assertThat(greeting).isEqualTo("Hello, world!");
    }
}
