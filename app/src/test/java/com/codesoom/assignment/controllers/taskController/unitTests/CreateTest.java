package com.codesoom.assignment.controllers.taskController.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TaskController 클래스")
public class CreateTest extends TaskControllerUnitTest {
    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        @BeforeEach
        void setUp() {
            when(taskServiceMock.createTask(task)).thenReturn(task);
        }

        @Test
        @DisplayName("새로운 Task를 생성하고 리턴한다.")
        void it_returns_a_task() {
            assertThat(taskController.create(task))
            .matches(output -> output.getId() == validId && taskTitle.equals(output.getTitle()));
        }

        @AfterEach
        void tearDown() {
            verify(taskServiceMock, atMostOnce())
                .createTask(argThat(input -> input == task));
        }
    }
}
