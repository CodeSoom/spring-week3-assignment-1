package com.codesoom.assignment.service;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

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
        id = 1L;
        notExistsId = 100L;
        title = "할 일";
        updateTitle = "수정된 할 일";
        source = new Task(title);
        updateSource = new Task(updateTitle);
    }

    @Test
    @DisplayName("할 일 생성")
    void createTask() {
        // when
        Task task = taskService.createTask(source);

        // then
        assertThat(task).isEqualTo(new Task(id, title));
    }

    @Test
    @DisplayName("할 일 가져오기")
    void getTask() {
        // given
        taskService.createTask(source);

        // when
        Task task = taskService.getTask(id);

        // then
        assertThat(task).isEqualTo(new Task(id, title));
    }

    @Test
    @DisplayName("할 일 가져오기 - 존재하지 않을 경우")
    void getNotExistsTask() {
        // given
        taskService.createTask(source);

        // when
        // then
        assertThatThrownBy(() -> taskService.getTask(notExistsId))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("할 일 항목 가져오기")
    void getTaskList() {
        // given
        taskService.createTask(source);

        // when
        List<Task> tasks = taskService.getTasks();

        // then
        assertThat(tasks.size()).isNotZero();
        assertThat(tasks.size()).isOne();
        assertThat(tasks.get(0)).isEqualTo(new Task(id, title));
        assertThat(tasks.get(0).getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("할 일 수정하기")
    void updateTask() {
        // given
        taskService.createTask(source);

        // when
        Task task = taskService.updateTask(id, updateSource);

        // then
        assertThat(task.getTitle()).isEqualTo(updateTitle);
    }

    @Test
    @DisplayName("할 일 삭제하기")
    void deleteTask() {
        // given
        taskService.createTask(source);

        // when
        Task task = taskService.deleteTask(id);

        // then
        assertThat(task.getTitle()).isEqualTo(source.getTitle());
    }
}
