package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TaskServiceTest {

  private TaskService taskService;

  private final Long NOT_FOUND_TASK_ID = 100L;
  private final Long NEW_TASK_ID = 1L;

  private final String NEW_TASK_TITLE = "TEST";
  private final String UPDATE_TASK_TITLE = "UPDATE_TASK_TITLE";

  private Task task;

  @BeforeEach
  void setUp() {
    TaskService taskService = new TaskService();
  }

  @Nested
  @DisplayName("getTasks 메소드는")
  class Describe_getTasks {

    @Nested
    @DisplayName("taskList가 비어있을때")
    class Context_WithEmptyList {

      @Test
      @DisplayName("빈 taskList를 return.")
      void it_return_emptyList() {
        List<Task> taskList = taskService.getTasks();

        Assertions.assertThat(taskList).isEmpty();
      }

    }

    @Nested
    @DisplayName("taskList가 비어있지 않을 때")
    class Context_WithListOfTasks {

      @BeforeEach
      void setUp() {
        task.setTitle(NEW_TASK_TITLE);
        taskService.createTask(task);
      }

      @Test
      @DisplayName("비어있지 않은 할 일 목록을 반환합니다.")
      void it_return_list() {
        List<Task> taskList = taskService.getTasks();
        Assertions.assertThat(taskList).isNotEmpty();
      }

    }

  }
}
