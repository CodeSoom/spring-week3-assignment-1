package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.*;


class TaskControllerTest {
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskController = new TaskController(new TaskService());
    }

    public Task createTask() {
        Task task = new Task();
        task.setTitle("task");
        return task;
    }

    @Test
    @DisplayName("최초 할일 생성 후 할일 리스트에 값이 늘어난지 테스트")
    void createTaskSuccessTest() {
        // given
        Task task = createTask();
        int beforeSize = taskController.list().size();
        // when
        Task insertTask = taskController.create(task);

        // then
        assertThat(
                beforeSize).isEqualTo(taskController.list().size() - 1);

    }

    @Test
    @DisplayName("최초 호출시 할일 리스트가 비어있는지 테스트.")
    void isFirstTasksIsEmpty() {
        assertThat(taskController.list()).isEmpty();
    }

    @Test
    @DisplayName("할일 복수 있는 경우 해당 개수에 맞게 나오는지 테스트.")
    void listSuccessTest() {
        // given
        Task task1 = createTask();
        Task task2 = createTask();

        // when
        taskController.create(task1);
        taskController.create(task2);

        // then
        assertThat(taskController.list()).hasSize(2);
    }


    @Test
    @DisplayName("할일을 1개를 등록 후 해당 할일 상세 조회 테스트")
    void getTaskSuccess() {
        // given
        Task task1 = createTask();

        // when
        taskController.create(task1);

        // then
        assertThat(taskController.detail(1L).getTitle()).isEqualTo("task1");

    }

    @Test
    @DisplayName("존재하지 않지 않는 할일 조회시 TaskNotFound 예외 발생 테스트")
    void getTaskFail() {
        assertThatThrownBy(() -> taskController.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할일 수정 후 해당 할일 값이 수정되었는지 테스트")
    void updateTaskTest() {
        // given
        Task task = taskController.create(createTask());
        Task updateTask = new Task();
        updateTask.setTitle("updated");

        // when
        taskController.update(task.getId(), updateTask);

        // then
        assertThat(taskController.detail(task.getId()).getTitle()).isEqualTo("updated");
    }

    @Test
    @DisplayName("존재하지 않는 할일을 수정할 경우 TaskNotFound 예외 발생 테스트")
    void updatedFailTest() {
        // given
        Task task = taskController.create(createTask());
        Task updateTask = new Task();
        updateTask.setTitle("updated");
        taskController.update(task.getId(), updateTask);
        // when then
        assertThatThrownBy(() -> taskController.update(100L, updateTask))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("1개 할일을 삭제 후 해당 리스트의 삭제후 리스트가 비어있는지 테스트")
    void deleteTaskSuccess() {
        // given
        Task task = taskController.create(createTask());
        // when
        taskController.delete(task.getId());
        // then
        assertThat(taskController.list()).isEmpty();
    }

    @Test
    @DisplayName("2개 할일을 삭제 후 해당 리스트의 삭제후 리스트크기가 감소했는 지 테스트")
    void deleteTasksSuccess() {
        // given
        taskController.create(createTask());
        Task task = taskController.create(createTask());
        int beforeSize = taskController.list().size();
        // when
        taskController.delete(task.getId());
        // then
        assertThat(taskController.list().size()).isEqualTo(beforeSize - 1);
    }

    @Test
    @DisplayName("존재하지 않는 할일을 삭제할 경우 TaskNotFound 예외 발생 테스트")
    void deleteTaskFail() {
        assertThatThrownBy(() -> taskController.delete(1L)).isInstanceOf(TaskNotFoundException.class);
    }
}
