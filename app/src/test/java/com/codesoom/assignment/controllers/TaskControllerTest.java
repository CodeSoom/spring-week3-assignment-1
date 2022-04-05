package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.referenceData.ReferenceData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);
        Task task = new Task();
        task.setTitle(ReferenceData.FIRST_TODO.getTitle());
        controller.create(task);
    }


    @Nested
    @DisplayName("할일 목록 조회 요청시")
    class WhenDetailRequest {

        @Nested
        @DisplayName("클라이언트가 전달한 아이디가 할일 목록에")
        class IsExistIdInTaskList {
            @Test
            @DisplayName("존재한다면 할일을 반환한다.")
            void existIdInTask() {
                assertThat(controller.detail(1L).getTitle()).isEqualTo(ReferenceData.FIRST_TODO);
            }

            @Test
            @DisplayName("존재하지 않는다면 예외를 발생한다")
            void notExistIdInTask() {
                assertThatThrownBy(() -> controller.detail(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

}