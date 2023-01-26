package com.codesoom.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private App app;

    @BeforeEach
    void setUp(){
        app = new App();
    }

    @Test
    void appHasGreeting() {
        assertThat(app.getGreeting()).isEqualTo("Hello, World!");
    }

    @Test
    void springApplicationStart() {
        App.main(new String[]{});
    }
}
