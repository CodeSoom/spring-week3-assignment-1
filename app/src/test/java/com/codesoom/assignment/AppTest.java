package com.codesoom.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("App")
public final class AppTest {
    private final App app = new App();

    @Test
    @DisplayName("getGreeting() should exist")
    void existTest() throws NoSuchMethodException, SecurityException {
        assertThat(app.getGreeting()).isNotEmpty();
    }

    @Test
    @DisplayName("getGreeting() should return \"Hello, world!\"")
    void returnTest() {
        assertThat(app.getGreeting()).isEqualTo("Hello, world!");
    }
}
