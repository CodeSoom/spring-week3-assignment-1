package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.dto.TaskRequestDto;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("할 일 Service 테스트")
class TaskServiceTest {

    private TaskService taskService;

    private static final String TITLE = "test";
    private static final String UPDATE_TITLE = "update test";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        TaskRequestDto source = new TaskRequestDto(TITLE);
        taskService.createTask(source);
    }

    @Test
    @DisplayName("할 일을 생성하면 생성된 할 일을 반환한다")
    void createTask() {
        Task task = taskService.createTask(new TaskRequestDto(TITLE));

        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("모든 할 일을 조회한다")
    void list() {
        List<Task> tasks = taskService.getTasks();
        Task task = tasks.get(0);

        assertThat(tasks).hasSize(1);
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("유효한 id로 할 일을 조회할 수 있다")
    void getTaskWithValidId() {
        Task task = taskService.getTask(1L);

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("유효하지 않은 id로 할 일을 조회할 경우 예외를 던진다")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(1000L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할 일을 수정하면 수정된 할 일을 반환한다")
    void updateTask() {
        Task task = taskService.updateTask(1L, new TaskRequestDto(UPDATE_TITLE));

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @Test
    @DisplayName("할 일을 삭제하면 삭제된 할 일을 반환한다")
    void deleteTask() {
        Task deletedTask = taskService.deleteTask(1L);

        assertThat(deletedTask.getId()).isEqualTo(1L);
        assertThat(deletedTask.getTitle()).isEqualTo(TITLE);
    }
}
