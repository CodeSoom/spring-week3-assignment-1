package com.codesoom.assignment.application;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskServiceTest 클래스")
class TaskServiceTest {
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class getTasks_메서드는 {
        TaskService taskService() {
            TaskService taskService = new TaskService();
            return taskService;
        }

        @Test
        @DisplayName("TaskResponse 리스트를 반환한다")
        void TaskResponse_리스트를_반환한다() {
            assertThat(taskService().getTasks()).hasSize(0).as("반환된 리스트는 크기가 0이다");
        }
    }
}
