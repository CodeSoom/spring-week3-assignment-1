package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Nested
@DisplayName("TaskErrorAdvice 클래스")
class TaskErrorAdviceTest {

    private final String ErrorResponse_MSG = "Task not found";

    @Nested
    @DisplayName("handleNotFound 메소드는")
    class Describe_handleNotFound {

        @Nested
        @DisplayName("TaskNotFoundException 예외가 발생하면")
        class Context_TaskNotFoundException_occur {

            @Test
            @DisplayName("ErrorResponse 객체를 리턴한다")
            void It_return_errorResponse() {

                TaskErrorAdvice taskErrorAdvice = new TaskErrorAdvice();
                Assertions.assertEquals(taskErrorAdvice.handleNotFound().getMessage(), ErrorResponse_MSG);

            }

        }

    }

}
