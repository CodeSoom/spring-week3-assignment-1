package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.dto.TaskRequestDto;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    private static final String TITLE = "test";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    @DisplayName("새로운 할 일을 목록에 추가할 수 있다")
    void createTask() {
        // given
        TaskRequestDto source = new TaskRequestDto(TITLE);

        // when
        taskService.createTask(source);

        // then
        Task task = taskService.getTask(1L);
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("유효한 id로 할 일을 조회할 수 있다")
    void getTaskWithValidId() {
        // given
        TaskRequestDto source = new TaskRequestDto(TITLE);
        taskService.createTask(source);

        // when
        Task task = taskService.getTask(1L);

        // then
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("유효하지 않은 id로 할 일을 조회할 경우 예외가 발생한다")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(1000L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    // TODO: update, delete 테스트 구현
}
