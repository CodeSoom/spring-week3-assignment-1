package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskControllerTest {
    private TaskController taskController;

    private TaskService taskService;

    @BeforeEach
    public void init(){
        taskController = new TaskController(taskService);
    }

    @Test
    public void list() {
        //given
        TaskService service = new TaskService();
        TaskController controller = new TaskController(taskService);
        //when

        //Then
        assertThat(controller.list()).isEmpty();
    }

    @Test
    public void createNewTask() {
        //given
        init();
        //when
        Task task = new Task();
        task.setTitle("Test");
        taskController.create(task);
        //Then
        assertThat(taskController.list()).isNotEmpty();
        assertThat(taskController.list()).hasSize(1);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo("Test");
        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
    }

}