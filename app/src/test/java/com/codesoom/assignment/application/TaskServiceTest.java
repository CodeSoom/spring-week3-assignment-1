package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 테스트")
class TaskServiceTest {
    public static final String TITLE = "NEW_TITLE";
    public static final String UPDATE_TITLE = "NEW_TITLE";

    TaskService taskService;

    @BeforeEach
    public void init() {
        taskService = new TaskService();
    }

    @Test
    @DisplayName("할 일이 비어있는 경우 getTasks 는 0을 응답한다.")
    public void fetchList() {
        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).hasSize(0);
    }

    @Test
    @DisplayName("존재하지 않는 특정 할 일을  상세 조회하는 경우 에러를 응답한다.")
    public void getThrowError() {
        assertThatThrownBy(() -> taskService.getTask(1L));
    }

    @Test
    @DisplayName(" 할 일에 대한  생성 요청을 하면 사이즈가 1증가한다.")
    public void create() {
        assertThat(taskService.getTasks()).hasSize(0);

        Task createdTask = createTestTask();
        taskService.createTask(createdTask);

        assertThat(taskService.getTasks()).hasSize(1);
    }

    @Test
    @DisplayName("특정 할 일이 수정되면 해당 할 일의 제목은 수정된다.")
    public void update() {
        Task task = createTestTask();
        Task createdTask = taskService.createTask(task);

        Task source = new Task();
        source.setTitle(UPDATE_TITLE);
        Task updatedTask = taskService.updateTask(createdTask.getId(), source);

        assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @Test
    @DisplayName("존재하지 않은 할 일을 수정하면 에러를 응답한다. ")
    public void updateWithInvalidTask() {
        assertThatThrownBy(() -> taskService.updateTask(1L, new Task()));
    }

    @Test
    @DisplayName("특정 할 일이  존재하는 경우 해당  할 일을 삭제하면 리스트의 사이즈는 줄어든다.")
    public void delete() {
        Task testTask = createTestTask();
        Task createdTask = taskService.createTask(testTask);
        taskService.deleteTask(createdTask.getId());
        assertThat(taskService.getTasks()).hasSize(0);
    }

    @Test
    @DisplayName("존재하지 않은 할 일을 삭제 하면 에러를 응답한다.")
    public void deleteWithEmptyList() {
        assertThatThrownBy(() -> taskService.deleteTask(9999L));
    }

    private Task createTestTask() {
        Task task = new Task();
        task.setTitle(TITLE);
        return task;
    }
}
