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
    @DisplayName("예외 객체를 생성한다")
    void handleNotFound() {
        ErrorResponse response = taskErrorAdvice.handleNotFound();

        assertThat(response).isEqualTo(new ErrorResponse(TaskErrorAdvice.ERROR_MESSAGE));
    }
}
