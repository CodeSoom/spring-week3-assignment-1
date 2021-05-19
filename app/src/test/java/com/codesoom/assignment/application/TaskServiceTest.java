package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private Long FIRST_TASK_ID = 1L;
    private Long SECOND_TASK_ID = 2L;

    private String FIRST_TASK_TITLE = "test1";
    private String SECOND_TASK_TITLE = "test2";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // subject
        taskService = new TaskService();

        // fixtures
        Task task1 = new Task();
        task1.setTitle(FIRST_TASK_TITLE);

        Task task2 = new Task();
        task2.setTitle(SECOND_TASK_TITLE);

        taskService.createTask(task1);
        taskService.createTask(task2);
    }

    @Test
    void testGetTasks() {
        assertThat(taskService.getTasks()).isNotEmpty();
    }

    @Test
    void testGetTask() {
        assertThat(taskService.getTask(FIRST_TASK_ID).getTitle())
                .isEqualTo(FIRST_TASK_TITLE);
        assertThat(taskService.getTask(SECOND_TASK_ID).getTitle())
                .isEqualTo(SECOND_TASK_TITLE);
        assertThatThrownBy(() -> taskService.getTask(10L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void testCreateTask() {
        Task task3 = new Task();
        assertThat(taskService.createTask(task3).getTitle()).isNull();
    }

    @Test
    void testUpdateTask() {
        String UPDATED_TITLE = "updated title";
        Task src = new Task();
        src.setId(FIRST_TASK_ID);
        src.setTitle(UPDATED_TITLE);
        assertThat(taskService.updateTask(FIRST_TASK_ID, src).getTitle())
                .isEqualTo(UPDATED_TITLE);
    }

    @Test
    void testDeleteTask() {
        assertThat(taskService.deleteTask(FIRST_TASK_ID).getId())
                .isEqualTo(FIRST_TASK_ID);
        assertThatThrownBy(() -> taskService.deleteTask(FIRST_TASK_ID))
                .isInstanceOf(TaskNotFoundException.class);
        assertThat(taskService.deleteTask(SECOND_TASK_ID).getTitle())
                .isEqualTo(SECOND_TASK_TITLE);
    }
 }