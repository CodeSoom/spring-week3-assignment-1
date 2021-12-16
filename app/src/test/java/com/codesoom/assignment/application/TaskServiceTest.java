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

        Task task = taskService.createTask(source);
    }

    @DisplayName("할일 목록을 조회하면 저장하고 있는 할일 컬렉션을 조회한다.")
    @Test
    void getTasks() {
        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).hasSize(1);
    }

    @DisplayName("할일을 조회하면 할일이 반환된다")
    @Test
    void getTask_ok() {
        Long taskId = 1L;
        Task task = taskService.getTask(taskId);

        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(taskId);
    }

    @DisplayName("잘못된 식별값으로 할일을 조회하면 예외가 터진다")
    @Test
    void getTask_error() {
        Long taskId = 100L;
        assertThatThrownBy(() -> taskService.getTask(taskId))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("할일을 추가하면 할일 목록의 크기가 커진다")
    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task source = new Task();
        taskService.createTask(source);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @DisplayName("할일을 수정하면 데이터가 수정된다")
    @Test
    void updateTask() {
        Long taskId = 1L;
        Task source = new Task();
        source.setTitle(NEW_TITLE + TITLE_POSTFIX);

        taskService.updateTask(taskId, source);
        Task task = taskService.getTask(taskId);

        assertThat(task.getId()).isEqualTo(taskId);
        assertThat(task.getTitle()).isEqualTo(NEW_TITLE + TITLE_POSTFIX);
    }

    @DisplayName("할일을 삭제하면 할일 목록에서 크기가 줄어든다")
    @Test
    void deleteTask() {
        Long taskId = 1L;
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(taskId);

        int newSize = taskService.getTasks().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }
}
