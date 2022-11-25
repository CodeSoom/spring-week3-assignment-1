package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TaskControllerTest {

    @Autowired
    TaskController taskController;

    @BeforeEach
    void before() {
        Task task = new Task();
        task.setTitle("test");
        taskController.createTask(task);
    }

    @Test
    @DisplayName("GET Tasks List가 비어있지 않음")
    void list() {
        assertThat(taskController.getTasks()).isNotEmpty();
    }

    @Test
    @DisplayName("GET id가 1L인 Task가 존재하는지 테스트")
    void detail() {
        assertThat(taskController.getTask(1L)).isNotNull();
        assertThat(taskController.getTask(1L).getTitle()).isEqualTo("test");
    }

    @Test
    @DisplayName("POST Task 객체 생성 테스트")
    void createTask() {
        Task newTask = new Task();
        newTask.setTitle("new Task");
        Task task = taskController.createTask(newTask);

        assertThat(task.getTitle()).isEqualTo("new Task");
    }

    //update put
    @Test
    @DisplayName("UPDATE id가 1L인 Task title 업데이트 테스트")
    void updateTask() {
        Task updateTask = new Task();
        updateTask.setTitle("update Task");
        Task task = taskController.updateTask(1L, updateTask);

        assertThat(task.getTitle()).isEqualTo("update Task");
    }

    //patch
    @Test
    @DisplayName("PATCH id가 1L인 Task title 업데이트 테스트")
    void patchTask() {
        Task patchTask = new Task();
        patchTask.setTitle("patch Task");
        Task task = taskController.updateTask(1L, patchTask);

        assertThat(task.getTitle()).isEqualTo("patch Task");
    }

    //delete
    @Test
    @DisplayName("DELETE Task 후 해당 ID로 찾을 때 TaskNotFoundException 던지는지 테스트")
    void deleteTask() {
        Task task = new Task();
        task.setTitle("delete Task");
        Task resultTask = taskController.createTask(task);

        taskController.deleteTask(resultTask.getId());
        assertThatThrownBy(() -> taskController.getTask(resultTask.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
