package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskErrorAdvice 클래스")
class TaskErrorAdviceTest {

    @Nested
    @DisplayName("handleNotFound 메소드는")
    class Describe_handleNotFound {
        TaskErrorAdvice taskErrorAdvice;

        @BeforeEach
        void prepareErrorResponse() {
            taskErrorAdvice = new TaskErrorAdvice();
        }

        @Test
        @DisplayName("객체를 생성하여 리턴합니다")
        void it_returns_errorObject() {
            assertThat(taskErrorAdvice.handleNotFound()).isInstanceOf(ErrorResponse.class);
        }
    }
}
