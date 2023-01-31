package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//TODO 컨트롤러 유닛테스트 작성하기.
class TaskControllerTest {
    private TaskService taskService;
    private TaskController taskController;

    private static final String TASK_TITLE = "Test1";

    @BeforeEach
    void setUp(){
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskController.create(task);
    }

    @Test
    void list() {
        assertThat(taskController.list()).isEmpty();
    }

    @Test
    void createTask() {
        final String secondTitle = "Test2";
        Task task = new Task();
        task.setTitle(secondTitle);
        taskController.create(task);

        List<Task> taskList = taskController.list();

        assertThat(taskList).hasSize(2);
        assertThat(taskList.get(0).getId()).isEqualTo(2L);
        assertThat(taskList.get(0).getTitle()).isEqualTo(secondTitle);
    }

    @Test
    void detailTask() {
        assertThat(taskController.detail(1L).getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void updateTask() {
        final Long taskId = 1L;
        final String changeTitle = "change";

        Task source = new Task();
        source.setTitle(changeTitle);

        taskController.update(taskId, source);
        assertThat(taskController.detail(taskId).getTitle()).isEqualTo(changeTitle);
    }

    @Test
    void patchTask() {
        final Long taskId = 1L;
        final String changeTitle = "change";

        Task source = new Task();
        source.setTitle(changeTitle);

        taskController.patch(taskId, source);
        assertThat(taskController.detail(taskId).getTitle()).isEqualTo(changeTitle);
    }

    @Test
    void deleteTask() {
        final Long taskId = 1L;

        taskController.delete(taskId);

        assertThat(taskController.list()).isEmpty();
    }
}
