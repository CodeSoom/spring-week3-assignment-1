package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.TaskRequestDto;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("할 일 Controller 유닛 테스트")
class TaskControllerTest {

    private static final String TITLE = "test";
    private static final String UPDATE_TITLE = "update test";
    private static final int TASKS_SIZE = 5;

    private TaskController taskController;

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);

        for (int id = 1; id <= TASKS_SIZE; id++) {
            taskController.create(new TaskRequestDto(TITLE + id));
        }
    }

    @Test
    @DisplayName("할 일을 생성할 수 있다")
    void createTask() {
    }

    @Test
    @DisplayName("모든 할 일을 조회할 수 있다")
    void list() {
        long id = 1L;
        List<Task> tasks = taskController.list();

        assertThat(tasks).hasSize(TASKS_SIZE);
        for (Task task : tasks) {
            assertThat(task.getId()).isEqualTo(id);
            assertThat(task.getTitle()).isEqualTo(TITLE + id);
            id += 1;
        }
    }

    @Test
    @DisplayName("id에 해당하는 할 일을 상세 조회할 수 있다")
    void detailTask() {
        Task task1 = taskController.detail(3L);
        Task task2 = taskController.detail(4L);

        assertThat(task1.getId()).isEqualTo(3L);
        assertThat(task1.getTitle()).isEqualTo(TITLE + 3L);
        assertThat(task2.getId()).isEqualTo(4L);
        assertThat(task2.getTitle()).isEqualTo(TITLE + 4L);
    }

    @Test
    @DisplayName("id에 해당하는 할 일이 없으면 예외가 발생한다")
    void detailTaskWithInvalidId() {
        assertThatThrownBy(() -> taskController.detail(1000L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("id에 해당하는 할 일을 찾아 수정할 수 있다")
    void updateTask() {
        Task updatedTask = taskController.update(1L, new TaskRequestDto(UPDATE_TITLE));

        assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @Test
    @DisplayName("id에 해당하는 할 일을 삭제할 수 있다")
    void deleteTask() {
        taskController.delete(3L);

        assertThat(taskController.list()).hasSize(TASKS_SIZE - 1);
    }
}
