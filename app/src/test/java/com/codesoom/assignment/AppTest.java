package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("App 클래스")
class AppTest {
    @Test
    @DisplayName("프로그램을 동작시킬 main메소드를 가지고 있는 App클래스가 제대로 생성되었는지 확인합니다.")
    void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
        assertEquals(classUnderTest.getGreeting(), "Hello, world!");
    }
}
