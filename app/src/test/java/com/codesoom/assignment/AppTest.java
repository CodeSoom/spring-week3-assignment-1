package com.codesoom.assignment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppTest {
    @Test
    void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
        assertEquals(classUnderTest.getGreeting(), "Hello, world!");

        assertThat(classUnderTest.getGreeting()).isEqualTo("Hello, world!");
    }
}
