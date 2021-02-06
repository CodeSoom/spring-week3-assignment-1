package com.codesoom.assignment.controllers;

        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.DisplayName;
        import org.junit.jupiter.api.Test;

        import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController 클래스")
public class HelloControllerTest {

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    @DisplayName("서버에서 응답을 보내는지 테스트")
    @Test
    void healthCheck() {
        assertThat(helloController.healthCheck()).isNotNull();
    }
}
