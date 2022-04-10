package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    @DisplayName("스프링 부트 애플리케이션을 성공적으로 실행한다.")
    @Test
    void springApplicationTest() {
        App.main(new String[]{});
    }

    @DisplayName("greeting()은 인사를 반환한다.")
    @Test
    void greetingTest() {
        final App app = new App();
        assertThat(app).isNotNull();
        assertThat(app.getGreeting()).isEqualTo("Hello, world!");
    }

}