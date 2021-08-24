package com.codesoom.assignment.controllers;


import static org.assertj.core.api.Assertions.assertThat;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TaskController에 대한 테스트")
class TaskControllerTest {

  private final String TASK_TITLE = "Test";

  private TaskController taskController;
  private Task task;

  @BeforeEach
  void setUp() {
    task = new Task();
    task.setTitle(TASK_TITLE);
    taskController = new TaskController(new TaskService());
  }

  @Nested
  @DisplayName("list메소드에서 ")
  class Describe_List {

    @Nested
    @DisplayName("저장된 task가 없을 때")
    class Context_WithoutAnyTask {

      @Test
      @DisplayName("빈 list를 return한다.")
      void ItReturnEmptyList() {
        assertThat(taskController.list()).isEmpty();

      }
    }

  }


}
