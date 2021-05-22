package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskServiceTest class")
class TaskServiceTest {

    private final String TASK_TITLE = "test";
    private final String TASK_UPDATE_POSTFIX = "UPDATED";

    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskService = new TaskService();

        // fixtures (미리 만들어지는 것들)
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Test
    @DisplayName("It returns tasks")
    void getTasks() {
        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).hasSize(1);

        Task task = tasks.get(0);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("It returns a task matches ID")
    void getTaskWithValidId() {
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("It throw the not found exception if ID mismatch")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(99L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("It creates new task, puts it in list, and return nothing")
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);

        assertThat(taskService.getTasks()).hasSize(2);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    @DisplayName("It delete a task matched ID and return nothing")
    void deleteTask() {
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(-1);
    }

    @Test
    @DisplayName("It update a task matched ID and return the task")
    void updateTask() {
        final Long ID = 1L;

        Task source = new Task();
        source.setTitle(TASK_TITLE + TASK_UPDATE_POSTFIX);

        Task task = taskService.updateTask(ID, source);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + TASK_UPDATE_POSTFIX);
    }
}
