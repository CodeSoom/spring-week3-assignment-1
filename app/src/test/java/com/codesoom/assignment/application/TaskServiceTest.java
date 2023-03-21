package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskService CRUD 테스트")
class TaskServiceTest {
    public static final String TITLE = "NEW_TITLE";
    public static final String UPDATE_TITLE = "NEW_TITLE";

    TaskService taskService;

    @BeforeEach
    public void init() {
        taskService = new TaskService();
    }

    @Test
    @DisplayName("생성 로직 테스트 생성하면 사이즈가 1증가한다.")
    public void create() {

        assertThat(taskService.getTasks()).hasSize(0);

        Task task = new Task();
        task.setTitle(TITLE);
        taskService.createTask(task);

        assertThat(taskService.getTasks()).hasSize(1);
    }

    @Test
    @DisplayName("수정하면 제목이 변경된다. ")
    public void update() {
        Task task = new Task();
        task.setTitle(TITLE);
        Task createTask = taskService.createTask(task);

        Task source = new Task();
        source.setTitle(UPDATE_TITLE);
        taskService.updateTask(createTask.getId(), source);

        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
    }
}
