package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskServiceTest {

  public static final String TITLE = "new task";
  private TaskService taskService;

  @BeforeEach
  void setUp() {
    taskService = new TaskService();

    Task source = new Task();
    source.setTitle(TITLE);
    taskService.createTask(source);
  }

  @Test
  void getTasks_ShouldReturnEmptyList() {
    assertThat(taskService.getTasks()).hasSize(1);
  }

  @Test
  void getTasks_ShouldReturnCreatedTasks() {
    List<Task> tasks = taskService.getTasks();
    assertThat(tasks).hasSize(1);
    assertThat(tasks.get(0).getTitle()).isEqualTo(TITLE);
  }

  @Test
  void createTask() {
    int oldSize = taskService.getTasks().size();

    Task source = new Task();
    source.setTitle("another task");

    taskService.createTask(source);

    int newSize = taskService.getTasks().size();
    int diff = newSize - oldSize;

    assertThat(diff).isEqualTo(1);
  }

  @Test
  void getTask_WithValidId() {
    Task task = taskService.getTask(1L);
    assertThat(task.getTitle()).isEqualTo(TITLE);
  }

  @Test
  void getTask_WithInvalidId() {
    assertThatThrownBy(() -> taskService.getTask(1001L))
        .isInstanceOf(TaskNotFoundException.class)
        .hasMessageMatching("Task not found: \\d+");
  }

  @Test
  void updateTask_WithValidId() {
    String newTaskTitle = "new task title";
    Task source = new Task();
    source.setTitle(newTaskTitle);

    Task updatedTask = taskService.updateTask(1L, source);

    assertThat(updatedTask.getTitle()).isEqualTo(newTaskTitle);
  }

  @Test
  void updateTask_WithInvalidId() {
    Task task = new Task();
    assertThatThrownBy(() -> taskService.updateTask(1001L, task))
        .isInstanceOf(TaskNotFoundException.class)
        .hasMessageMatching("Task not found: \\d+");
  }

  @Test
  void deleteTask_WithValidId() {
    int oldSize = taskService.getTasks().size();

    taskService.deleteTask(1L);

    int newSize = taskService.getTasks().size();
    int diff = newSize - oldSize;

    assertThat(diff).isEqualTo(-1);
  }

  @Test
  void deleteTask_WithInvalidId() {
    assertThatThrownBy(() -> taskService.deleteTask(1001L))
        .isInstanceOf(TaskNotFoundException.class)
        .hasMessageMatching("Task not found: \\d+");
  }
}