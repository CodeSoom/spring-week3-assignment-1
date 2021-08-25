package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskIdGenerator;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskController 테스트")
class TaskControllerTest {
    private TaskController taskController;

    private static final String TASK_TITLE_ONE = "test1";
    private static final String TASK_TITLE_TWO = "test2";
    private static final String TASK_UPDATE_TITLE = "updateTitle";
    private Task task1;
    private Task task2;

    @BeforeEach
    void setup() {
        TaskService taskService = new TaskService(new TaskIdGenerator());
        taskController = new TaskController(taskService);

        TaskService.clear();
        task1 = taskService.createTask(Task.from(TASK_TITLE_ONE));
        task2 = taskService.createTask(Task.from(TASK_TITLE_TWO));
    }

    @DisplayName("할 일 전체를 조회할 수 있습니다.")
    @Test
    void list() {
        final List<Task> list = taskController.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("할 일이 없으면 빈 리스트가 조회됩니다.")
    @Test
    void listWithoutEntity() {
        TaskService.clear();
        final List<Task> list = taskController.list();

        assertThat(list).isEmpty();
    }

    @DisplayName("아이디를 통해 할 일을 조회 할 수 있습니다.")
    @Test
    void detail() {
        Task task = taskController.detail(task1.getId());

        assertThat(task).isEqualTo(task1);

        task = taskController.detail(task2.getId());

        assertThat(task).isEqualTo(task2);
    }

    @DisplayName("유효하지 않은 아이디로 할 일을 검색하면 예외가 발생합니다.")
    @Test
    void detailInvalid() {
        assertThatThrownBy(()-> taskController.detail(10L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("새로운 할 일을 등록할 수 있습니다.")
    @ParameterizedTest
    @ValueSource(strings = {"test3", "test4", "test5"})
    void createTask(String title) {
        final Task savedTask = taskController.create(Task.from(title));

        final Task findTask = taskController.detail(savedTask.getId());

        assertThat(findTask.getTitle()).isEqualTo(title);
        assertThat(findTask).isEqualTo(savedTask);
    }

    @DisplayName("할 일을 수정할 수 있습니다.")
    @Test
    void updateTask() {
        taskController.update(task1.getId(), Task.from(TASK_UPDATE_TITLE));
        final Task findTask = taskController.detail(task1.getId());

        assertThat(findTask.getTitle()).isEqualTo(TASK_UPDATE_TITLE);
    }

    @DisplayName("존재하지 않는 식별자의 할 일을 수정하려 할 경우 예외가 발생합니다.")
    @Test
    void updateTaskInvalid() {
        assertThatThrownBy(()-> taskController.update(10L, Task.from(TASK_UPDATE_TITLE)))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("할 일을 수정할 수 있습니다.")
    @Test
    void patchTask() {
        taskController.patch(task1.getId(), Task.from(TASK_UPDATE_TITLE));
        final Task findTask = taskController.detail(task1.getId());

        assertThat(findTask.getTitle()).isEqualTo(TASK_UPDATE_TITLE);
    }

    @DisplayName("존재하지 않는 식별자의  할 일을 수정하려 할 경우 예외가 발생합니다.")
    @Test
    void patchTaskInvalid() {
        assertThatThrownBy(()-> taskController.patch(10L, Task.from(TASK_UPDATE_TITLE)))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("할 일을 삭제할 수 있습니다.")
    @Test
    void deleteTask() {
        final int originLength = taskController.list().size();

        taskController.delete(task1.getId());

        final int afterLength = taskController.list().size();

        assertThat(originLength - afterLength).isEqualTo(1);

    }

    @DisplayName("존재하지 않는 할 일을 삭제하려 하면 예외가 발생합니다. ")
    @Test
    void deleteTaskInvalid() {
        taskController.delete(task1.getId());

        assertThatThrownBy(()-> taskController.delete(task1.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
