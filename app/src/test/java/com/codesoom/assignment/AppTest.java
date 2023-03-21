package com.codesoom.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class AppTest {

    App app;

    @BeforeEach
    public void init() {
        app = new App();
    }

    @Test
    @DisplayName("getGreetingTest")
    public void getGreeting() {
        assertThat(app.getGreeting()).isEqualTo("Hello, world!");
    }

    @Test
    @DisplayName("mainTest - 커버러지만 채움 정확히 어떻게 테스트하는지 다시 조사 필요")
    public void run() {
        App.main(new String[]{"123"});
    }

}
