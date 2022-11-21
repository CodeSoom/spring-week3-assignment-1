package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskControllerTest {

    @Autowired
    TaskController taskController;

    @Autowired
    TaskService taskService;

    private static final Long TASK_ID = 1L;
    private final Task task = new Task();

    @Test
    @Order(0)
    void list() {
        assertThat(taskController.list()).isEmpty();
    }

    @Test
    @Order(1)
    void create() {
        task.setTitle("test");
        Task resultTask = taskController.create(task);

        assertThat(taskService.getTasks()).isNotEmpty();
        assertThat(resultTask.getId()).isEqualTo(TASK_ID);
    }

    @Test
    @Order(2)
    void detail() {
        assertThat(taskController.detail(TASK_ID)).isNotNull();
    }

    @Test
    @Order(3)
    void update() {
        task.setTitle("update title");
        taskController.update(TASK_ID, task);

        assertThat(taskController.detail(TASK_ID).getTitle())
                .isEqualTo("update title");
    }

    @Test
    @Order(4)
    void patch() {
        task.setTitle("patch title");
        taskController.update(TASK_ID, task);

        assertThat(taskController.detail(TASK_ID).getTitle())
                .isEqualTo("patch title");
    }

    @Test
    @Order(5)
    void delete() {
        taskController.delete(TASK_ID);

        assertThat(taskController.list()).isEmpty();
    }

}
