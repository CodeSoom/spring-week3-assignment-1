package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskService service;
    private TaskController controller;
    private final String TASK_TITLE = "Test Task";
    private final Long TASK_ID = 1L;
    private final Long TASK_ID_NOT_EXISTING = 0L;
    private final int FIRST = 0;

    @BeforeEach
    void setUp() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        service = new TaskService();
        service.createTask(task);
        controller = new TaskController(service);
    }

    @Test
    @DisplayName("기본 생성된 Task가 하나만 존재하면, 목록조회 요청 시 반환값 내 Task의 개수는 1개여야 한다")
    void whenList_returnEqualSize() {
        List<Task> expected = service.getTasks();
        List<Task> actual = controller.list();

        assertThat(actual).hasSameSizeAs(expected);
    }

    @Test
    @DisplayName("기본 생성된 Task가 하나만 존재하면, 목록조회 요청 시 반환된 Task id는 기본 생성된 Task의 id와 같아야한다")
    void whenList_returnEqualId() {
        Long actual = controller.list().get(FIRST).getId();

        assertThat(actual).isEqualTo(TASK_ID);
    }

    @Test
    @DisplayName("기본 생성된 Task가 하나만 존재하면, 목록조회 요청 시 반환된 Task title은 기본 생성된 Task의 title과 같아야한다")
    void whenList_returnEqualTitle() {
        String actual = controller.list().get(FIRST).getTitle();

        assertThat(actual).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("기본 생성된 하나만 존재하고, Task Id로 정상적으로 상세조회 요청 시 반환된 Task id는 기본 생성된 Task의 id와 같아야한다")
    void whenDetail_returnEqualId() {
        Long actual = controller.detail(TASK_ID).getId();

        assertThat(actual).isEqualTo(TASK_ID);
    }

    @Test
    @DisplayName("기본 생성된 하나만 존재하고, Task Id로 정상적으로 상세조회 요청 시 반환된 Task title은 기본 생성된 Task의 title과 같아야한다")
    void whenDetail_returnEqualTitle() {
        String actual = controller.detail(TASK_ID).getTitle();

        assertThat(actual).isEqualTo(TASK_TITLE);
    }
}
