package com.codesoom.assignment;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    private App app;

    @BeforeEach
    void setUp(){
        app = new App();
    }

    @Test
    @DisplayName("SpringBoot 어플리케이션이 구동되면, / path 로 요청이 들어오면, 리스폰스 바디 포함할 Hello World 를 돌려준다")
    void getGreeting() {
        assertThat(this.app.getGreeting()).isEqualTo("Hello, world!");
    }

    @Test
    public void applicationContextLoaded() {
    }

    @Disabled("Disabled due to long test time")
    @Test
    public void applicationContextTest() {
        App.main(new String[] {});
    }
}
