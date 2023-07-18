package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


class TaskControllerTest {
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskController = new TaskController(new TaskService());
    }

    public Task createTask() {
        Task task = new Task();
        task.setTitle("task");
        return task;
    }

    private void createTasks() {
        for (int i = 1; i <= 10; i++) {
            Task task = new Task();
            task.setTitle("task" + i);
            taskController.create(task);
        }
    }

    @Test
    @DisplayName("할일 생성 성공 테스트.")
    void createTaskSuccessTest() {
        Task task = createTask();
        taskController.create(task);
        Assertions.assertThat(taskController.list()).hasSize(1);
    }

    @Test
    @DisplayName("최초 할일 리스트가 비어있는지 테스트.")
    void isFirstTasksIsEmpty() {
        Assertions.assertThat(taskController.list()).isEmpty();
    }

    @Test
    @DisplayName("할일 리스트가 정상적으로 나오는 지 테스트한다.")
    void listSuccessTest() {
        createTasks();
        Assertions.assertThat(taskController.list()).hasSize(10);
    }


    @Test
    @DisplayName("할일 상세 조회 성공 테스트")
    void getTaskSuccess() {
        createTasks();
        Assertions.assertThat(taskController.detail(4L).getTitle()).isEqualTo("task4");
    }

    @Test
    @DisplayName("할일 상세 조회 실패 테스트")
    void getTaskFail() {
        createTasks();
        Assertions.assertThatThrownBy(() -> taskController.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할일 수정 성공 테스트")
    void updateTaskTest() {
        Task task = taskController.create(createTask());
        Task updateTask = new Task();
        updateTask.setTitle("updated");
        taskController.update(task.getId(), updateTask);
        Assertions.assertThat(taskController.detail(task.getId()).getTitle()).isEqualTo("updated");
    }

    @Test
    @DisplayName("할일 수정 실패 테스트")
    void updatedFailTest() {
        Task task = taskController.create(createTask());
        Task updateTask = new Task();
        updateTask.setTitle("updated");
        taskController.update(task.getId(), updateTask);
        Assertions.assertThatThrownBy(() -> taskController.update(100L, updateTask))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할일 삭제 성공 테스트")
    void deleteTaskSuccess() {
        taskController.create(createTask());
        taskController.delete(1L);
        Assertions.assertThat(taskController.list()).isEmpty();
    }

    @Test
    @DisplayName("할일 삭제 실패 테스트")
    void deleteTaskFail() {
        Assertions.assertThatThrownBy(() -> taskController.delete(1L)).isInstanceOf(TaskNotFoundException.class);
    }
}
