package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    @Test
    @DisplayName("greeting test")
    void testGreeting() {
        App app = new App();
        String greeting = app.getGreeting();

        assertThat(greeting).isEqualTo("Hello, world!");
    }

    @Test
    @DisplayName("spring boot application 시작 테스트")
    void testMain() {
        App.main(new String[] {});
    }
}
