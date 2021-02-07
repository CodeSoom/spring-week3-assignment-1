package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;

    private static final Long ORIGINAL_ID = 1L;
    private static final String ORIGINAL_TITLE = "test";
    private static final String POST_FIX = "!!!";

    @BeforeEach
    void setUp(){
       taskService = mock(TaskService.class);
       controller = new TaskController(taskService);

       List<Task> tasks = new ArrayList<>();
       Task task = new Task();
       task.setTitle(ORIGINAL_TITLE);
       tasks.add(task);

       given(taskService.getTasks()).willReturn(tasks);
       given(taskService.getTask(ORIGINAL_ID)).willReturn(task);
       given(taskService.getTask(100L))
               .willThrow(new TaskNotFoundException(100L));
       given(taskService.updateTask(eq(100L), any(Task.class)))
               .willThrow(new TaskNotFoundException(100L));
       given(taskService.deleteTask(100L))
               .willThrow(new TaskNotFoundException(100L));
       controller = new TaskController(taskService);


    }

    @Test
    void listWithSomeTasks() {
        assertThat(controller.list()).isNotEmpty();

        verify(taskService).getTasks();
    }

    @Test
    void listWithoutTasks() {
        given(taskService.getTasks()).willReturn(new ArrayList<>());

        assertThat(controller.list()).isEmpty();

        verify(taskService).getTasks();
    }


    @Test
    void detailWithValid() {
        Task task = controller.detail(ORIGINAL_ID);

        assertThat(task).isNotNull();
    }


    @Test
    void detailWithInvalid() {
        assertThatThrownBy(() -> controller.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }


    @Test
    void create() {
        Task task = new Task();
        task.setTitle(ORIGINAL_TITLE);

        controller.create(task);

        verify(taskService).createTask(task);
    }


    @Test
    void updateWithValid() {
        Task task = new Task();
        task.setTitle(ORIGINAL_TITLE + POST_FIX);

        controller.update(ORIGINAL_ID, task);

        verify(taskService).updateTask(1L, task);

    }

    @Test
    void updateWithInvalid() {
        Task task = new Task();
        task.setTitle(ORIGINAL_TITLE + POST_FIX);

        assertThatThrownBy(() -> controller.update(100L, task))
                .isInstanceOf(TaskNotFoundException.class);

    }

    @Test
    void deleteWithValid() {
        controller.delete(ORIGINAL_ID);

        verify(taskService).deleteTask(ORIGINAL_ID);
    }


    @Test
    void deleteWithInvalid() {
        assertThatThrownBy(() -> controller.delete(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

}