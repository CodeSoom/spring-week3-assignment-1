package com.codesoom.assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("AppTest 클래스")
class AppTest {
    @Test
    @DisplayName("Context가 로드되는 지 확인합니다.")
    void contextLoad() {
        App.main(new String[]{});
    }
}