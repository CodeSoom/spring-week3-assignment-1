package com.codesoom.assignment;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AppTest {

    @Test
    @DisplayName("Hello, world! 출력")
    void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting());
        assertEquals(classUnderTest.getGreeting(), "Hello, world!");

        assertThat(classUnderTest.getGreeting()).isEqualTo("Hello, world!");
    }

    @Test
    @DisplayName("App 메인 메서드 실행")
    public void main() {
        App.main(new String[] {});
    }
}
