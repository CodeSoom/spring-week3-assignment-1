package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.ErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TaskErrorAdvice 클래스")
class TaskErrorAdviceTest {

    private TaskErrorAdvice taskErrorAdvice;
    private ErrorResponse errorResponse;

    private TaskController taskController;
    private TaskService taskService;


    private final static Long TASK_TEST_ID = 1L;
    private final static String TASK_NOT_FOUND_MESSAGE = "Task not found: %d";

    @BeforeEach
    void setUp() {
        this.taskErrorAdvice = new TaskErrorAdvice();
        this.errorResponse = new ErrorResponse("Task not found");
        this.taskService = new TaskService();
        this.taskController = new TaskController(taskService);
    }


    @Test
    @DisplayName("handleNotFound메소드는 찾는 할 일이 없으면 'Task not found' 메시지를 반환하고 예외를 발생한다.")
    void handleNotFound() {
        assertThat(taskErrorAdvice.handleNotFound()).isInstanceOf(ErrorResponse.class);
        assertThat(taskErrorAdvice.handleNotFound().getMessage()).isEqualTo(errorResponse.getMessage());
        assertThatExceptionOfType(TaskNotFoundException.class)
                .isThrownBy(() -> taskController.detail(TASK_TEST_ID))
                .withMessage(String.format(TASK_NOT_FOUND_MESSAGE, TASK_TEST_ID));

    }
}