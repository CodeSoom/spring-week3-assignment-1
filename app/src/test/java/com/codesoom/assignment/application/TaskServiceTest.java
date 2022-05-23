package com.codesoom.assignment.application;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class TaskServiceTest {

    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)

    private TaskService taskService;
    private static final String TASK_TITLE = "test";

    @BeforeEach
    void setUp(){
        taskService = new TaskService();

        // fixtures
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }


    @Test
    void getTasks(){
        assertThat(taskService.getTasks()).hasSize(1);

        Task task = taskService.getTasks().get(0);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTask(){

        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("test");
        // assertThatThrownBy(() -> taskService.getTask(1L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void updateTask(){
        Task task = new Task();
        task.setTitle("updated test");
        taskService.updateTask(1L, updatedTask);
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("updated test");
    }

    @Test
    void deleteTask(){
        int originSize = taskService.getTasks().size();
        taskService.deleteTask(1L);
        int newSize = taskService.getTasks().size();
        assertThat(originSize-newSize).isEqualTo(1);
    }


}