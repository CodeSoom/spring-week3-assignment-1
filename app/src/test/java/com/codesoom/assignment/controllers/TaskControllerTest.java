package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TaskControllerTest {

    private static final String title = "Task 1";
    private Task task;
    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        task = new Task();
        task.setTitle(title);
    }

    @DisplayName("할일 목록이 비었을 때 빈 리스트 반환 테스트")
    @Test
    void emptyListTest() {
        assertThat(taskController.list()).hasSize(0);
    }

    @DisplayName("할일 목록이 비어있지 않을 때 리스트 반환 테스트")
    @Test
    void notEmptyListTest() {
        taskService.createTask(new Task());
        taskService.createTask(new Task());

        assertThat(taskController.list()).hasSize(2);
    }

    @DisplayName("유효한 `id`로 할일 가져오기 테스트")
    @Test
    void taskWithValidId() {
        taskService.createTask(task);

        assertThat(taskController.detail(1L).getTitle()).isEqualTo("Task 1");
    }

    @DisplayName("유효하지 않은 `id`로 할일 가져오면 TaskNotFoundException 발생 테스트")
    @Test
    void taskWithInValidId() {
        taskService.createTask(task);

        assertThatThrownBy(() -> taskController.detail(100L).getTitle()).isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("할 일 만든 후 생성된 결과 리턴")
    @Test
    void createTaskReturnResult() {
        Task resultTask = taskController.create(task);

        assertThat(resultTask.getId()).isEqualTo(1L);
        assertThat(resultTask.getTitle()).isEqualTo(title);
    }

    @DisplayName("기존에 생성된 할 일의 제목 수정 후 수정된 결과 리턴 : Update 이용")
    @Test
    void updateTaskReturnResult() {

        String newTitle = "New " + title;
        Task resultTask = taskService.createTask(task);
        resultTask.setTitle(newTitle);

        Task updateResultTask = taskController.update(resultTask.getId(), resultTask);

        assertThat(updateResultTask.getTitle()).isEqualTo(newTitle);
    }

    @DisplayName("기존에 생성된 할 일의 제목 수정 후 수정된 결과 리턴 : Patch 이용")
    @Test
    void patchTaskReturnResult() {
        String newTitle = "New " + title;
        Task resultTask = taskService.createTask(task);
        resultTask.setTitle(newTitle);

        Task updateResultTask = taskController.patch(resultTask.getId(), resultTask);

        assertThat(updateResultTask.getTitle()).isEqualTo(newTitle);
    }

    @DisplayName("할일 목록에서 `id`로 삭제")
    @Test
    void deleteTask() {
        Task resultTask = taskService.createTask(task);

        int originalSize = taskService.getTasks().size();
        taskController.delete(resultTask.getId());
        int newSize = taskService.getTasks().size();

        assertThat(originalSize - newSize).isEqualTo(1);
    }
}
