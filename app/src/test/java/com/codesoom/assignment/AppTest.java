package com.codesoom.assignment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppTest {

    private App app;

    @BeforeEach
    void setUp() {
        app = new App();
    }

    @Test
    void appGetGreeting() {
        Assertions.assertThat(app.getGreeting()).isEqualTo("Hello, world!");
    }

    @Test
    void appMain() {
        app.main(new String[] {});
    }
}
