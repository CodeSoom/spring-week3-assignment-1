package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    // TODO
    // 1. list -> getTasks
    // 2. detail -> getTasks (with ID)
    // 3. create -> createTasks (with source)
    // 4. update -> update Task (with ID, source)
    // 5. delete -> delete Task (with ID)
    private TaskService taskService;
    final String TASK_TITLE = "test";

    @BeforeEach
    void setUp(){
        taskService = new TaskService();
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

    }

    @Test
    void getTasks(){
        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).hasSize(1);

        Task task = tasks.get(0);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithValidId(){
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);

    }
    @Test
    void getTaskWithInvalidId(){
        assertThatThrownBy(() -> taskService.getTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

}