package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskControllerTest {

  public static final String TITLE = "Test Task";
  private TaskController taskController;

  @BeforeEach
  void setUp() {
    taskController = new TaskController(new TaskService());

    Task task = new Task();
    task.setTitle(TITLE);
    taskController.create(task);
  }

  @Test
  void list() {
    assertThat(taskController.list()).hasSize(1);
  }

  @Test
  void create() {
    Task task = new Task();
    task.setTitle("Test Task2");

    taskController.create(task);

    List<Task> tasks = taskController.list();
    assertThat(tasks).hasSize(2);
    assertThat(tasks.get(0).getTitle()).isEqualTo(TITLE);
    assertThat(tasks.get(1).getTitle()).isEqualTo("Test Task2");
  }

  @Test
  void detail_WithValidId() {
    Task foundTask = taskController.detail(1L);
    assertThat(foundTask.getId()).isEqualTo(1L);
    assertThat(foundTask.getTitle()).isEqualTo(TITLE);
  }

  @Test
  void detail_WithInvalidId() {
    assertThatThrownBy(() -> taskController.detail(1001L))
        .isInstanceOf(TaskNotFoundException.class)
        .hasMessageMatching("Task not found: \\d+");
  }

  @Test
  void putUpdateTask_WithValidId() {
    Task source = new Task();
    source.setTitle("Updated Title");

    Task updatedTask = taskController.update(1L, source);

    assertThat(updatedTask.getId()).isEqualTo(1L);
    assertThat(updatedTask.getTitle()).isEqualTo("Updated Title");
  }

  @Test
  void putUpdateTask_WithInvalidId() {
    Task source = new Task();
    assertThatThrownBy(() -> taskController.update(1001L, source))
        .isInstanceOf(TaskNotFoundException.class)
        .hasMessageMatching("Task not found: \\d+");
  }

  @Test
  void patchUpdateTask_WithValidId() {
    Task source = new Task();
    source.setTitle("Updated Title");

    Task updatedTask = taskController.patch(1L, source);

    assertThat(updatedTask.getId()).isEqualTo(1L);
    assertThat(updatedTask.getTitle()).isEqualTo("Updated Title");
  }

  @Test
  void patchUpdateTask_WithInvalidId() {
    Task source = new Task();
    assertThatThrownBy(() -> taskController.patch(1001L, source))
        .isInstanceOf(TaskNotFoundException.class)
        .hasMessageMatching("Task not found: \\d+");
  }

  @Test
  void delete_WithValidId() {
    int oldSize = taskController.list().size();

    taskController.delete(1L);

    int newSize = taskController.list().size();
    int diff = newSize - oldSize;
    assertThat(diff).isEqualTo(-1);
  }

  @Test
  void delete_WithInvalidId() {
    assertThatThrownBy(() -> taskController.delete(1001L))
        .isInstanceOf(TaskNotFoundException.class)
        .hasMessageMatching("Task not found: \\d+");
  }
}