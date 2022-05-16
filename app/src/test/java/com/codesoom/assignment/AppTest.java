package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    @DisplayName("getGreeting test")
    void app_GetGreeting() {
        App app = new App();
        String greeting = app.getGreeting();
        assertThat(greeting).isEqualTo("Hello, world!");
    }
}
