package com.codesoom.assignment.service;

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

class TaskServiceTest {

    private TaskService taskService;
    private Task source;
    private Task updateSource;

    @BeforeEach
    void setup() {
        taskService = new TaskService();
        source = new Task(TaskConstant.TITLE);
        updateSource = new Task(TaskConstant.UPDATE_TITLE);
        taskService.createTask(source);
    }

    @Test
    @DisplayName("할 일 생성 및 반환")
    void createTask() {
        // when
        Task task = taskService.createTask(source);

        // then
        assertThat(task.getTitle()).isEqualTo(TaskConstant.TITLE);
        assertThat(task.getId()).isNotNull();
    }

    @Test
    @DisplayName("할 일 가져오기")
    void getTask() {
        // when
        Task task = taskService.getTask(TaskConstant.ID);

        // then
        assertThat(task).isEqualTo(new Task(TaskConstant.ID, TaskConstant.TITLE));
    }

    @Test
    @DisplayName("할 일 가져오기 - 존재하지 않을 경우")
    void getNotExistsTask() {
        // when
        // then
        assertThatThrownBy(() -> taskService.getTask(TaskConstant.NOT_EXISTS_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할 일 항목 가져오기")
    void getTaskList() {
        // when
        List<Task> tasks = taskService.getTasks();

        // then
        assertThat(tasks.size()).isNotZero();
        assertThat(tasks.get(0)).isEqualTo(new Task(TaskConstant.ID, TaskConstant.TITLE));
    }

    @Test
    @DisplayName("할 일 수정 및 반환")
    void updateTask() {
        // when
        Task task = taskService.updateTask(TaskConstant.ID, updateSource);

        // then
        assertThat(task.getTitle()).isEqualTo(TaskConstant.UPDATE_TITLE);
    }

    @Test
    @DisplayName("할 일 삭제하기")
    void deleteTask() {
        // when
        Task task = taskService.deleteTask(TaskConstant.ID);

        // then
        assertThat(task.getTitle()).isEqualTo(source.getTitle());
    }
}
