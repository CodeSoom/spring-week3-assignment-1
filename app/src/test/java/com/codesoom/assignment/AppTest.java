package com.codesoom.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("App 클래스")
class AppTest {

    private App app;

    @BeforeEach
    void setUp() {
        app = new App();
    }

    @Test
    @DisplayName("getGreeting 메소드는 'Hello, world!'를 반환한다.")
    void getGreeting() {
        assertThat(app.getGreeting()).isEqualTo("Hello, world!");
    }

    @Test
    @DisplayName("main 메소드는 Spring Application를 실행한다.")
    void main() {
        App.main(new String[]{});
    }
}