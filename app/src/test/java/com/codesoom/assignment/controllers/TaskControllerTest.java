package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    TaskController taskController;

    @BeforeEach
    void before() {
        Task task = new Task();
        task.setTitle("test");
        taskController.create(task);
    }

    @Test
    @DisplayName("Tasks List가 비어있지 않음")
    void list() {
        assertThat(taskController.list()).isNotEmpty();
    }

    @Test
    @DisplayName("Task 객체 생성 테스트")
    void createTask() {
        Task newTask = new Task();
        newTask.setTitle("new Task");
        Task task = taskController.create(newTask);

        assertThat(task.getId()).isGreaterThan(1L);
    }

    @Test
    @DisplayName("id가 1L인 Task가 존재하는지 테스트")
    void detail() {
        assertThat(taskController.detail(1L)).isNotNull();
        assertThat(taskController.detail(1L).getTitle()).isEqualTo("test");
    }

    //update put
    @Test
    @DisplayName("id가 1L인 Task title 업데이트 테스트")
    void updateTask() {
        Task updateTask = new Task();
        updateTask.setTitle("update Task");
        Task task = taskController.update(1L, updateTask);

        assertThat(task.getTitle()).isEqualTo("update Task");
    }

    //patch
    @Test
    void patchTask() {
        Task patchTask = new Task();
        patchTask.setTitle("patch Task");
        Task task = taskController.patch(1L, patchTask);

        assertThat(task.getTitle()).isEqualTo("patch Task");
    }

    //delete
    @Test
    void deleteTask() {
        Task task = new Task(2L, "delete Task");
        taskController.create(task);
        assertThat(taskController.detail(2L)).isNotNull();

        taskController.delete(2L);
        assertThatThrownBy(() -> taskController.detail(2L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
