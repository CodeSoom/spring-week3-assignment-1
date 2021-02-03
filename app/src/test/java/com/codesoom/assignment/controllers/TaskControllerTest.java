package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    private TaskController controller;

    private static final Long ORIGINAL_ID = 1L;
    private static final String ORIGINAL_TITLE = "test";

    @BeforeEach
    void setUp(){
       controller = new TaskController();
    }

    @Test
    void list() {
        assertThat(controller.list()).hasSize(1);
    }


    @Test
    void detailWithValid() {
        Task found = controller.detail(ORIGINAL_ID);

        assertThat(found.getId()).isEqualTo(ORIGINAL_ID);
        assertThat(found.getTitle()).isEqualTo(ORIGINAL_TITLE);

    }

    @Test
    void detailWithInvalid() {
        assertThatThrownBy(() -> controller.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }


    @Test
    void create() {
        int oldSize = controller.list().size();

        Task task = new Task();
        task.setTitle(ORIGINAL_TITLE);
        controller.create(task);

        int newSize = controller.list().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }


    @Test
    @DisplayName("check ID and its name")
    void createNewTask(){
        Task task = new Task();
        task.setTitle("Test1");
        controller.create(task);

        assertThat(controller.list()).hasSize(1);
        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
        assertThat(controller.list().get(0).getTitle()).isEqualTo("Test1");

    }

}