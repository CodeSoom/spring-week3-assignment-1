package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskControllerTest {
    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task createTask = new Task();
        createTask.setTitle("Test1");
        taskController.create(createTask);
    }

    @Test
    @DisplayName("TaskController 클래스의 list 메소드는 List<Task>를 반환한다")
    void listTasks() {
        assertThat(taskController.list()).hasSize(1);
    }

    @Test
    @DisplayName("TaskController 클래스의 detail 메소드는 id가 있다면 해당 Task를 반환한다")
    void detailTask() {
        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo("Test1");
    }

    @Test
    @DisplayName("TaskController 클래스의 create 메소드는 title을 입력받아 Task를 생성한다")
    void createTask() {
        Task createTask = new Task();
        createTask.setTitle("Test2");
        taskController.create(createTask);

        assertThat(taskController.list()).hasSize(2);
        assertThat(taskController.list().get(1).getId()).isEqualTo(2L);
        assertThat(taskController.list().get(1).getTitle()).isEqualTo("Test2");
    }

    @Test
    @DisplayName("TaskController 클래스의 update 메소드는 id가 있다면 해당 Task를 수정한다")
    void updateTask() {
        Task updateTask = new Task();
        updateTask.setTitle("new Test");
        taskController.update(1L, updateTask);

        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo("new Test");
    }

    @Test
    @DisplayName("TaskController 클래스의 patch 메소드는 id가 있다면 해당 Task를 수정한다")
    void patchTask() {
        Task updateTask = new Task();
        updateTask.setTitle("new Test");
        taskController.patch(1L, updateTask);

        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo("new Test");
    }

    @Test
    @DisplayName("TaskController 클래스의 delete 메소드는 id가 있다면 해당 Task를 삭제한다")
    void deleteTask() {
        taskController.delete(1L);

        assertThat(taskController.list()).isEmpty();
    }
}
