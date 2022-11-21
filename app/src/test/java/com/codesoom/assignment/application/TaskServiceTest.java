package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @BeforeEach
    void before() {
        Task task = new Task();
        task.setTitle("test");
        taskService.createTask(task);
    }

    @Test
    void getTasks() {
        assertThat(taskService.getTasks()).isNotEmpty();
    }

    @Test
    void getTask() {
        Long id = 1L;
        assertThat(taskService.getTask(id)).isNotNull();
    }

    @Test
    void createTask() {
        Task source = new Task();
        source.setTitle("test2");
        taskService.createTask(source);

        assertThat(taskService.getTask(2L)).isNotNull();
    }

    @Test
    void updateTask() {
        Long id = 1L;
        Task task = new Task();
        task.setTitle("change title");

        taskService.updateTask(id, task);

        assertThat(taskService.getTask(id).getTitle()).isNotEqualTo("test");
    }

    @Test
    void deleteTask() {
        taskService.deleteTask(2L);

        assertThatThrownBy(() -> taskService.getTask(2L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
