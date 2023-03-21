package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @DisplayName("생성하지 않은 리스트를 조회하면 사이즈는 0이다")
    public void fetchList() {
        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).hasSize(0);
    }

    @Test
    @DisplayName("빈 리스트를 상세조회할 경우 에러 반환")
    public void getThrowError(){
        assertThatThrownBy(() -> taskService.getTask(1L));
    }

    @Test
    @DisplayName("생성 로직 테스트 생성하면 사이즈가 1증가한다.")
    public void create() {
        assertThat(taskService.getTasks()).hasSize(0);

        Task task = createTestTask();
        taskService.createTask(task);

        assertThat(taskService.getTasks()).hasSize(1);
    }

    @Test
    @DisplayName("수정하면 제목이 변경된다. ")
    public void update() {
        Task task = createTestTask();
        Task createTask = taskService.createTask(task);

        Task source = new Task();
        source.setTitle(UPDATE_TITLE);
        taskService.updateTask(createTask.getId(), source);

        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @Test
    @DisplayName("삭제를 하면 사이즈가 0이된다. ")
    public void delete() {
        Task testTask = createTestTask();
        Task task = taskService.createTask(testTask);
        taskService.deleteTask(task.getId());
        assertThat(taskService.getTasks()).hasSize(0);
    }

    private Task createTestTask() {
        Task task = new Task();
        task.setTitle(TITLE);
        return task;
    }
}
