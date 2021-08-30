package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    @Test
    @DisplayName("Main 테스트")
    void main() {
        // given
        App app = new App();

        // when
        // then
        app.main(new String[]{});
    }

    @Test
    @DisplayName("Hello, world! 유닛 테스트")
    void getGreeting() {
        // given
        App app = new App();

        // when
        String hello = app.getGreeting();

        // then
        assertThat(hello).isEqualTo("Hello, world!");
    }
}
