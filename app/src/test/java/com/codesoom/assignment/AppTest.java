package com.codesoom.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    private App app;

    @BeforeEach
    void setUp(){
        app = new App();
    }

    @Test
    void getGreeting() {
        assertThat(this.app.getGreeting()).isEqualTo("Hello, world!");
    }
}