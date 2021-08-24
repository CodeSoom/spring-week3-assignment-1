package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class TaskControllerTest {

    TaskController controller = new TaskController(new TaskService());

    @BeforeEach
    @DisplayName("초기화")
    void setUp() {
        Task task1 = new Task();
        task1.setTitle("test1");

        Task task2 = new Task();
        task2.setTitle("test2");

        controller.create(task1);
        controller.create(task2);
    }

    @Test
    @DisplayName("전체조회 확인")
    void list() {
        assertThat(controller.list()).hasSize(2);
    }


    @Test
    @DisplayName("상세조회")
    void detail() {

        Task detail = controller.detail(1L);
        assertThat("test1").isEqualTo(detail.getTitle());

    }

    @Test
    @DisplayName("아이디 생성 및 등록하기")
    void create() {

        Task task3 = new Task();
        task3.setTitle("test3");
        controller.create(task3);

        assertThat(controller.detail(3L).getId()).isEqualTo(3L);
        assertThat(controller.detail(3L).getTitle()).isEqualTo(task3.getTitle());

    }



    @Test
    @DisplayName("수정하기")
    void update() {

        Task updateTask = new Task();
        updateTask.setTitle("test1 update");

        Task result = controller.update(1L, updateTask);
        assertThat("test1 update").isEqualTo(result.getTitle());

    }

    @Test
    @DisplayName("삭제하기")
    void delete() {

        controller.delete(2L);
        assertThat(controller.list()).hasSize(1);

    }

}
