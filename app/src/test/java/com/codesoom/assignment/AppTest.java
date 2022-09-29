package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("APP 클래스")
class AppTest {
    @Nested
    @DisplayName("getGreeting 메소드는")
    class Describe_getGreeting {
        private final String expected = "Hello, world!";

        @Test
        @DisplayName("해당 메소드를 호출하면 주어진 문자열을 리턴한다")
        void it_returns_string() {
            // when
            App classUnderTest = new App();
            String actual = classUnderTest.getGreeting();

            assertThat(actual).isEqualTo(expected);
        }


    }

    @Test
    @DisplayName("Spring Application이 잘 작동하는지 확인한다.")
    void contextLoads() {
        App.main(new String[]{});
    }
}