package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("App main class")
class AppTest {
    @Test
    @DisplayName("It runs the spring application")
    void application() {
        App.main(new String[] {});
    }

    @Test
    @DisplayName("It returns the greeting")
    void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }
}
