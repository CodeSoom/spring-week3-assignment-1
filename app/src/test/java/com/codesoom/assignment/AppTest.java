package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    private App app;

    private final String helloWorld = "Hello, world!";

    @BeforeEach
    void setUp() {
        app = new App();
    }

    @Test
    void testGetGreeting() {
        final String greeting = app.getGreeting();
        assertThat(greeting).isEqualTo(helloWorld);
    }
}
