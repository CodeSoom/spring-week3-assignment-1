package com.codesoom.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    @DisplayName("인사말이 올바른지 검증한다")
    void getGreeting() {
        App app = new App();

        String greeting = app.getGreeting();

        assertThat(greeting).isEqualTo("Hello, world!");
    }
}
