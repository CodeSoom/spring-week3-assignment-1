package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private TaskService taskService;

    private final static String NEW_TITLE = "new task";
    private final static String TITLE_POSTFIX = " spring";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task source = new Task();
        source.setTitle(NEW_TITLE);

        taskService.createTask(source);
    }

    @DisplayName("getTasks 메소드는 저장하고 있는 할 일 목록을 반환한다.")
    @Test
    void getTasks() {
        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).hasSize(1);
    }

    @DisplayName("getTask 메소드는 주어진 할 일을 반환한다")
    @Test
    void getTask_ok() {
        Long taskId = 1L;
        Task task = taskService.getTask(taskId);

        assertThat(task.getId()).isEqualTo(taskId);
    }

    @DisplayName("getTask 메소드는 주어지지 않는 할 일이면 예외를 던진다")
    @Test
    void getTask_error() {
        Long taskId = 100L;

        assertThatThrownBy(() -> taskService.getTask(taskId))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("createTask 메소드는 할 일을 만들고 목록에 추가한다")
    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task source = new Task();
        taskService.createTask(source);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @DisplayName("updateTask 메소드는 할 일을 수정한다")
    @Test
    void updateTask() {
        Task source = new Task();
        source.setTitle(NEW_TITLE + TITLE_POSTFIX);

        Long taskId = 1L;
        taskService.updateTask(taskId, source);

        Task task = taskService.getTask(taskId);

        assertThat(task.getTitle()).isEqualTo(NEW_TITLE + TITLE_POSTFIX);
    }

    @DisplayName("deleteTask 메소드는 할 일 목록에서 주어진 할 일을 삭제한다")
    @Test
    void deleteTask() {
        int oldSize = taskService.getTasks().size();

        Long taskId = 1L;
        taskService.deleteTask(taskId);

        int newSize = taskService.getTasks().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }
}
