package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
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
    private String title;
    private String updateTitle;
    private Long id;
    private Long notExistsId;
    private Task source;
    private Task updateSource;

    @BeforeEach
    void setup() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
        id = 1L;
        notExistsId = 100L;
        title = "할 일";
        updateTitle = "수정된 할 일";
        source = new Task(title);
        updateSource = new Task(updateTitle);
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
        assertThat(list.size()).isOne();
    }

    @Test
    @DisplayName("할 일 생성")
    void createTask() {
        // when
        Task task = taskController.create(source);

        // then
        assertThat(task).isEqualTo(new Task(id, title));
    }

    @Test
    @DisplayName("할 일 가져오기")
    void getTask() {
        // given
        taskController.create(source);

        // when
        Task task = taskController.detail(id);

        // then
        assertThat(task).isEqualTo(new Task(id, title));
    }

    @Test
    @DisplayName("할 일 가져오기 - 존재하지 않을 경우")
    void getNotExistsTask() throws Exception {
        // given
        taskController.create(source);

        // when
        // then
        assertThatThrownBy(() -> taskService.getTask(notExistsId))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할 일 수정 - PUT")
    void modifyTask() {
        // given
        taskController.create(source);

        // when
        Task task = taskController.update(id, updateSource);

        // then
        assertThat(task.getTitle()).isEqualTo(updateTitle);
    }

    @Test
    @DisplayName("할 일 수정 - PATCH")
    void modifyTaskByPatch() {
        // given
        taskController.create(source);

        // when
        Task task = taskController.patch(id, updateSource);

        // then
        assertThat(task.getTitle()).isEqualTo(updateTitle);
    }

    @Test
    @DisplayName("할 일 삭제")
    void deleteTask() {
        // given
        taskController.create(source);

        // when
        taskController.delete(id);

        // then
        assertThatThrownBy(() -> taskController.detail(id))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
