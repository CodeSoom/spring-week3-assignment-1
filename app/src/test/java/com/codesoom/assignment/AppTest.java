package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("App 클래스")
public class AppTest {

    @DisplayName("앱의 정상 동작 확인을 위한 테스트")
    @Test
    void greeting() {
        App app = new App();
        assertThat(app.getGreeting()).isEqualTo("Hello, world!");
    }
}
