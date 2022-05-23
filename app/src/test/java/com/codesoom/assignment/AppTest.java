package com.codesoom.assignment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    @Test
    void appHasAGreeting() {
        App classUnderTest = new App();
        String result = classUnderTest.getGreeting();
        assertThat(result).isEqualTo("Hello, world!!");
    }
}
