package com.codesoom.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    private App app;

    @BeforeEach
    void setup() {
        app = new App();
    }

    @Test
    void appHasAGreeting() {
        assertThat(app.getGreeting()).isEqualTo("Hello, world!");
    }

    @Test
    void appRunsSpringApplication() {
        app.main(new String[] {});
    }
}
