package com.codesoom.assignment.application;

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
    @DisplayName("Tasks가 empty가 아닌지 테스트")
    void getTasks() {
        assertThat(taskService.getTasks()).isNotEmpty();
    }

    @Test
    @DisplayName("존재하는 ID로 찾을 경우 BeforeEach에서 생성했던 title과 일치하는지 확인")
    void getTaskWithValidId() {
        assertThat(taskService.getTask(1L)).isNotNull();
        assertThat(taskService.getTasks().contains(taskService.getTask(1L))).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 ID로 찾을 경우 TaskNotFoundException 발생")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Task 생성 테스트")
    void createTask() {
        Task source = new Task();
        source.setTitle("test2");
        taskService.createTask(source);

        assertThat(taskService.getTask(2L)).isNotNull();
    }

    @Test
    @DisplayName("title 변경 후 이전 title과 다른지 확인")
    void updateTask() {
        Long id = 1L;
        Task task = new Task();
        task.setTitle("change title");

        taskService.updateTask(id, task);

        assertThat(taskService.getTask(id).getTitle()).isNotEqualTo("test2");
    }

    @Test
    @DisplayName("Task 삭제 후 getTask할 경우 TaskNotFoundException 발생 테스트")
    void deleteTask() {
        taskService.deleteTask(2L);

        assertThatThrownBy(() -> taskService.getTask(2L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
