package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
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
    void Given_Task가_하나만_존재_When_목록_조회_요청_Then_반환값_내_Task_개수는_Service_반환값_개수와_같다() {
        List<Task> expected = service.getTasks();
        List<Task> actual = controller.list();

        assertThat(actual).hasSameSizeAs(expected);
    }

    @Test
    void Given_Task가_하나만_존재_When_목록_조회_요청_Then_반환된_Task_id_는_Service_내_Task_id와_같아야한다() {
        Long actual = controller.list().get(FIRST).getId();

        assertThat(actual).isEqualTo(TASK_ID);
    }

    @Test
    void Given_Task가_하나만_존재_When_목록_조회_요청_Then_반환된_Task_title_는_Service_내_Task_title와_같아야한다() {
        String actual = controller.list().get(FIRST).getTitle();

        assertThat(actual).isEqualTo(TASK_TITLE);
    }

    @Test
    void Given_Task가_하나만_존재_When_상세_조회_요청_Then_반환된_Task_id_는_Service_내_Task_id와_같아야한다() {
        Long actual = controller.detail(TASK_ID).getId();

        assertThat(actual).isEqualTo(TASK_ID);
    }

    @Test
    void Given_Task가_하나만_존재_When_상세_조회_요청_Then_반환된_Task_title_는_Service_내_Task_title와_같아야한다() {
        String actual = controller.detail(TASK_ID).getTitle();

        assertThat(actual).isEqualTo(TASK_TITLE);
    }
}
