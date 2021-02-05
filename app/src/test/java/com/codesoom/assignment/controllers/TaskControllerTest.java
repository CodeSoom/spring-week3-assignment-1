package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;

class TaskControllerTest {

    TaskController taskController;
    TaskService taskService;
    Task task;

    @BeforeEach
    void init(){
        taskService = new TaskService();
        taskController = new TaskController(taskService);
        task = new Task();
        task.setId(1L);
        task.setTitle("책읽기");
    }
    
}
