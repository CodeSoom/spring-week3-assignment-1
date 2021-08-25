package com.codesoom.assignment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AppTest {

    @Test
    void contextLoads() {
        App.main(new String[]{});
    }

    @Test
    void greetingTest() {
        final App app = new App();
        final String greeting = app.getGreeting();

        assertThat(greeting).isEqualTo("Hello, world!");
    }
}
