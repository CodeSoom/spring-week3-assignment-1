package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.TaskIdGenerator;
import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TaskServiceTest {

    private static final String TASK_TITLE = "TEST";
    private static final String POSTFIX_TITLE = "UPDATE";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // subject
        taskService = new TaskService(new TaskIdGenerator());

        // fixtures
        taskService.createTask(new Task(1L, TASK_TITLE));
    }

    @Test
    @DisplayName("할 일 전체를 조회한다")
    void getTasks() {
        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).hasSize(1);
    }

    @Test
    @DisplayName("단일 조회 시 존재하는 할 일 이라면 찾은 할 일을 반환한다")
    void getTaskWithValidId() {
        Task found = taskService.getTask(1L);

        assertThat(found).isEqualTo(new Task(1L, TASK_TITLE));
    }

    @Test
    @DisplayName("단일 조회 시 할 일을 찾을 수 없다면 에러를 던진다")
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(2L))
            .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할 일을 생성한다")
    void createTask() {
        int oldSize = taskService.getTasks().size();

        long newId = 2L;
        taskService.createTask(new Task(newId, TASK_TITLE));

        int newSize = taskService.getTasks().size();
        assertThat(newSize - oldSize).isEqualTo(1);

        Task found = taskService.getTask(newId);
        assertThat(found.getId()).isEqualTo(newId);
        assertThat(found.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("수정 시 할 일을 찾았다면 수정한다")
    void updateTaskWithExistTask() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + POSTFIX_TITLE);

        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + POSTFIX_TITLE);
    }

    @Test
    @DisplayName("수정 시 할 일을 찾을 수 없다면 에러를 던진다")
    void updateTaskWithNotExistTask() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + POSTFIX_TITLE);

        assertThatThrownBy(() -> taskService.updateTask(2L, source))
            .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("삭제시 할 일을 찾았다면 삭제한다")
    void deleteTaskWithExistTask() {
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();
        assertThat(oldSize - newSize).isEqualTo(1);
    }

    @Test
    @DisplayName("삭제시 할 일을 찾을 수 없다면 에러를 던진다")
    void deleteTaskWithNotExistTask() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + POSTFIX_TITLE);

        assertThatThrownBy(() -> taskService.deleteTask(2L))
            .isInstanceOf(TaskNotFoundException.class);
    }
}
