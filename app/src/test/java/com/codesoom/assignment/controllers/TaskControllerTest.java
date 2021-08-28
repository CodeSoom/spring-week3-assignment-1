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
  private final String CREATE_TITLE = "createTest";
  private final String MODIFY_TITLE = "modifyTest";

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

  @Nested
  @DisplayName("Create메소드에서")
  class Describe_Create {

    @Nested
    @DisplayName("Task객체가 주어질때")
    class Context_WithValidTask {

      private Task validTask;

      @BeforeEach
      void setUp() {
        validTask = new Task();
        validTask.setTitle(CREATE_TITLE);
      }

      @Test
      @DisplayName("주어진 객체의 title로 생성한 task객체를 return")
      void It_ReturnNewTaskByValidTaskTitle() {
        Task createdTask = taskController.create(validTask);
        assertThat(createdTask.getTitle()).isEqualTo(validTask.getTitle())
            .withFailMessage("주어진 task의 title을 바탕으로 생성한 task를 return해야합니다.");

      }
    }

  }

  @Nested
  @DisplayName("Update메소드에서")
  class Describe_Update {

    @Nested
    @DisplayName("Task객체와 id가 주어질때")
    class Context_WithValidTaskAndId {

      private Task beforeTask;
      private Long givenId;

      @BeforeEach
      void setUp() {
        beforeTask = new Task();
        beforeTask.setTitle(MODIFY_TITLE);

        givenId = taskController.create(task).getId();

      }


      @Test
      @DisplayName("giveId의 Task를 수정하고, 수정된 Task를 return")
      void It_ReturnModifiedTask() {
        Task modifiedTask = taskController.update(givenId, beforeTask);
        assertThat(modifiedTask.getTitle()).isEqualTo(beforeTask.getTitle())
            .withFailMessage("주어진 task의 title을 바탕으로 생성한 task를 return해야합니다.");

      }
    }

  }

  @Nested
  @DisplayName("Patch메소드에서")
  class Describe_Patch {

    @Nested
    @DisplayName("Task객체와 id가 주어질때")
    class Context_WithValidTaskAndId {

      private Task modifyTask;
      private Long givenId;

      @BeforeEach
      void setUp() {
        modifyTask = new Task();
        modifyTask.setTitle(MODIFY_TITLE);

        givenId = taskController.create(task).getId();

      }


      @Test
      @DisplayName("giveId의 Task를 수정하고, 수정된 Task를 return")
      void It_ReturnModifiedTask() {
        Task modifiedTask = taskController.patch(givenId, modifyTask);
        assertThat(modifyTask.getTitle()).isEqualTo(modifiedTask.getTitle())
            .withFailMessage("주어진 task의 title을 바탕으로 생성한 task를 return해야합니다.");

      }
    }

  }

  @Nested
  @DisplayName("Delete메소드에서")
  class Describe_Delete {

    @Nested
    @DisplayName("유효한 id가 주어질때")
    class Context_WithValidId {

      private Long validId;
      private Task deletedTask;

      @BeforeEach
      void setUp() {
        deletedTask = taskController.create(task);
        validId = deletedTask.getId();

      }


      @Test
      @DisplayName("해당 id의 task를 삭제한다.")
      void It_ReturnModifiedTask() {
        taskController.delete(validId);

        assertThatThrownBy(
            () -> taskController.detail(validId),
            "저장되지 않은 task를 조회할때의 에러가 throw."
        ).isInstanceOf(TaskNotFoundException.class);
      }
    }

  }
}
