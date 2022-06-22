package com.codesoom.assignment;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
