package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TaskServiceTest {

    private static final String title = "Task 1";
    private static final String newTitle = "Task2";
    private TaskService taskService;
    private Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        task = new Task();
        task.setTitle(title);

        taskService.createTask(task);
    }

    @DisplayName("할일이 추가 됐을 때 기존 목록 갯수 + 1 인 리스트 반환")
    @Test
    void getNotEmptyTasksTest() {
        int originalSize = taskService.getTasks().size();
        taskService.createTask(new Task());

        assertThat(taskService.getTasks()).hasSize(originalSize + 1);
    }

    @DisplayName("할일 생성 후 생성된 할일 리턴")
    @Test
    void createTaskTest() {
        Task newTask = taskService.createTask(task);

        assertThat(newTask.getId()).isEqualTo(2L);
    }

    @DisplayName("`id`로 할일을 찾아서 할일 수정")
    @Test
    void updateTaskTest() {
        Task newTask = new Task();
        newTask.setTitle(newTitle);

        Task resultTask = taskService.updateTask(1L, newTask);

        assertThat(resultTask.getId()).isEqualTo(1L);
        assertThat(resultTask.getTitle()).isEqualTo(newTitle);
    }

    @DisplayName("할일 목록이 1개인 리스트에서 `id`로 할일 삭제 후 빈 리스트 반환")
    @Test
    void deleteTaskTest() {
        taskService.deleteTask(1L);

        assertThat(taskService.getTasks()).hasSize(0);
    }
}
