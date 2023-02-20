package com.codesoom.assignment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppTest {
    @Test
    void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }

    @Test
    public void sayHello() throws Exception{
        //given
        App app = new App();
        //when
        //Then
//        assertEquals("Hello, world!", result);
        assertThat(app.getGreeting()).isEqualTo("Hello, world!");

    }
}
