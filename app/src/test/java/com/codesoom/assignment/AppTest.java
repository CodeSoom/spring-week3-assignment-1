package com.codesoom.assignment;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppTest {

    @Test
    void appStartTest() {
        App.main(new String[]{});
    }

    @Test
    void greetingTest() {
        App app = new App();

        assertThat(app.getGreeting()).isEqualTo("Hello, world!");
    }
}
