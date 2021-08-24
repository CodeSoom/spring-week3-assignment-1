package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    TaskService taskService = new TaskService();

    @BeforeEach
    @DisplayName("초기화")
    void setUp() {

        Task task1 = new Task();
        task1.setTitle("test1");

        taskService.createTask(task1);
    }

    @Test
    @DisplayName("전체조회")
    void list() {
        assertThat(taskService.getTasks()).hasSize(1);
    }

    @Test
    @DisplayName("상세조회")
    void detail() {
        assertThat("test1").isEqualTo(taskService.getTask(1L).getTitle());
    }

    @Test
    @DisplayName("상세조회 예외")
    void detailThrow() {

        assertThatThrownBy(() -> {
            taskService.getTask(100L);})
                .isInstanceOf(TaskNotFoundException.class);

    }

    @Test
    @DisplayName("아이디생성 및 등록하기")
    void create() {

        Task task2 = new Task();
        task2.setTitle("test2");
        taskService.createTask(task2);

        assertThat(2L).isEqualTo(taskService.getTask(2L).getId());
        assertThat("test2").isEqualTo(taskService.getTask(2L).getTitle());

    }

    @Test
    @DisplayName("수정하기")
    void update() {

        Task updateTask = new Task();
        updateTask.setTitle("test1 update");

        assertThat(taskService.updateTask(1L, updateTask).getTitle()).isEqualTo("test1 update");

    }

    @Test
    @DisplayName("삭제하기")
    void delete() {

        taskService.deleteTask(1L);
        assertThat(taskService.getTasks()).isEmpty();

    }

}
