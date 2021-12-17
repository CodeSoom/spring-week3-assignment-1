package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("App 클래스")
class AppTest {

    @Nested
    @DisplayName("getGreeting() 메소드는 ")
    class Describe_getGreeting {
        @Test
        @DisplayName("서버 생존용 문자열을 리턴합니다.")
        void it_return_hello_world() {
            App app = new App();

            assertNotNull(app.getGreeting());
        }
    }

}
