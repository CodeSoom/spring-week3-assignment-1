package com.codesoom.assignment.controllers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TaskController에 대한 테스트")
class TaskControllerTest {

  private final String TASK_TITLE = "Test";

  private TaskController taskController;
  private Task task;
  private Long NotFoundId = 0L;

  @BeforeEach
  void setUp() {
    task = new Task();
    task.setTitle(TASK_TITLE);
    taskController = new TaskController(new TaskService());
  }

  @Nested
  @DisplayName("list메소드에서")
  class Describe_List {

    @Nested
    @DisplayName("저장된 task가 없을 때")
    class Context_WithoutAnyTask {

      @Test
      @DisplayName("빈 list를 return")
      void ItReturnEmptyList() {
        assertThat(taskController.list()).isEmpty();

      }
    }

    @Nested
    @DisplayName("저장된 task가 있을 때")
    class Context_WithTask {

      @BeforeEach
      void setUp() {

        taskController.create(task);

      }

      @Test
      @DisplayName("저장된 task가 담긴 리스트를 return")
      void ItReturnListWithTask() {
        List<Task> taskList = taskController.list();
        assertThat(taskController.list()).isEqualTo(taskList);
      }
    }

  }

  @Nested
  @DisplayName("detail메소드에서")
  class Describe_Detail {

    @Nested
    @DisplayName("저장되지 않은 task의 id로 조회할 때")
    class Context_NotFoundTaskId {

      @BeforeEach
      void setUp() {
        taskController.create(task);
      }

      @Test
      @DisplayName("404상태코드와 에러메세지를 return")
      void It_Return404AndErrorMessage() {
        assertThatThrownBy(
            () -> taskController.detail(NotFoundId),
            "저장되지 않은 task를 조회할때의 에러가 throw."
        ).isInstanceOf(TaskNotFoundException.class);
      }
    }

  }


}
