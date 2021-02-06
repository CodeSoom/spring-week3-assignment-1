package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;

    private static final Long ORIGINAL_ID = 1L;
    private static final String ORIGINAL_TITLE = "test";
    private static final String POST_FIX = "!!!";

    @BeforeEach
    void setUp(){
        taskService = new TaskService();
       controller = new TaskController(taskService);

       Task task = new Task();
       task.setTitle(ORIGINAL_TITLE);
       controller.create(task);
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
    void updateWithValid() {
        Task source = new Task();
        source.setTitle(ORIGINAL_TITLE+POST_FIX);
        controller.update(ORIGINAL_ID, source);

        Task task = controller.detail(ORIGINAL_ID);
        assertThat(task.getTitle()).isEqualTo(ORIGINAL_TITLE+POST_FIX);
    }

    @Test
    void updateWithInvalid() {
        Task source = new Task();
        source.setTitle(ORIGINAL_TITLE+POST_FIX);

        assertThatThrownBy(() -> controller.update(100L, source))
                .isInstanceOf(TaskNotFoundException.class);

        assertThatThrownBy(() -> controller.update(200L, source))
                .isInstanceOf(TaskNotFoundException.class);
    }

}