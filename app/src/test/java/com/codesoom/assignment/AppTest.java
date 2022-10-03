package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class AppTest {
    @Test
    @DisplayName("spring 앱을 정상적으로 실행한다")
    void name() {
        App.main(new String[]{});
    }
}