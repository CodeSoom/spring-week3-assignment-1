package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.constant.TaskConstant;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;
    private Task source;
    private Task updateSource;

    @BeforeEach
    void setup() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
        source = new Task(TaskConstant.TITLE);
        updateSource = new Task(TaskConstant.UPDATE_TITLE);
    }

    @Test
    @DisplayName("할 일 항목 가져오기")
    void getTaskList() {
        // given
        taskController.create(source);

        // when
        List<Task> list = taskController.list();

        // then
        assertThat(list.size()).isNotZero();
    }

    @Test
    @DisplayName("할 일 생성")
    void createTask() {
        // when
        Task task = taskController.create(source);

        // then
        assertThat(task).isEqualTo(new Task(TaskConstant.ID, TaskConstant.TITLE));
    }

    @Test
    @DisplayName("할 일 가져오기")
    void getTask() {
        // given
        taskController.create(source);

        // when
        Task task = taskController.detail(TaskConstant.ID);

        // then
        assertThat(task).isEqualTo(new Task(TaskConstant.ID, TaskConstant.TITLE));
    }

    @Test
    @DisplayName("할 일 가져오기 - 존재하지 않을 경우")
    void getNotExistsTask() throws Exception {
        // given
        taskController.create(source);

        // when
        // then
        assertThatThrownBy(() -> taskService.getTask(TaskConstant.NOT_EXISTS_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할 일 수정 - PUT")
    void modifyTask() {
        // given
        taskController.create(source);

        // when
        Task task = taskController.update(TaskConstant.ID, updateSource);

        // then
        assertThat(task.getTitle()).isEqualTo(TaskConstant.UPDATE_TITLE);
    }

    @Test
    @DisplayName("할 일 수정 - PATCH")
    void modifyTaskByPatch() {
        // given
        taskController.create(source);

        // when
        Task task = taskController.patch(TaskConstant.ID, updateSource);

        // then
        assertThat(task.getTitle()).isEqualTo(TaskConstant.UPDATE_TITLE);
    }

    @Test
    @DisplayName("할 일 삭제")
    void deleteTask() {
        // given
        taskController.create(source);

        // when
        taskController.delete(TaskConstant.ID);

        // then
        assertThatThrownBy(() -> taskController.detail(TaskConstant.ID))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
