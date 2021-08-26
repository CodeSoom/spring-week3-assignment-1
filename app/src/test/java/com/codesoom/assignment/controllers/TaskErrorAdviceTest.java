package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.codesoom.assignment.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TaskErrorAdviceTest {

    private TaskErrorAdvice taskErrorAdvice;

    @BeforeEach
    void setUp() {
        taskErrorAdvice = new TaskErrorAdvice();
    }

    @Test
    @DisplayName("404 응답을 받을 시 할 일을 찾을 수 없다는 메시지를 가진 에러 객체를 생성한다")
    void handleNotFound() {
        String expectedMessage = "Task not found";

        assertThat(taskErrorAdvice.handleNotFound())
            .isEqualTo(new ErrorResponse(expectedMessage));
    }
}
