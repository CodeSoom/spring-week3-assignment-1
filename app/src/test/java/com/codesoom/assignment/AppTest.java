package com.codesoom.assignment;

import com.codesoom.assignment.App;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
        assertEquals(classUnderTest.getGreeting(), "Hello, world!");
    }
}
