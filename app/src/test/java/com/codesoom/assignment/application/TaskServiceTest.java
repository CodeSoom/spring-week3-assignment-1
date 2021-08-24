package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskServiceTest {

    private static final String TASK_TITLE = "TEST";
    private static final String POSTFIX_TITLE = "UPDATE";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // subject
        taskService = new TaskService();

        // fixtures
        taskService.createTask(new Task(1L, TASK_TITLE));
    }

    @Test
    void getTasks() {
        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).hasSize(1);
    }

    @Test
    void getTaskWithValidId() {
        Task found = taskService.getTask(1L);

        assertThat(found).isEqualTo(new Task(1L, TASK_TITLE));
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(2L))
            .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();

        long newId = 2L;
        taskService.createTask(new Task(newId, TASK_TITLE));

        int newSize = taskService.getTasks().size();
        assertThat(newSize - oldSize).isEqualTo(1);

        Task found = taskService.getTask(newId);
        assertThat(found.getId()).isEqualTo(newId);
        assertThat(found.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void updateTask() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + POSTFIX_TITLE);

        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + POSTFIX_TITLE);
    }

    @Test
    void deleteTask() {
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();
        assertThat(oldSize - newSize).isEqualTo(1);
    }
}
