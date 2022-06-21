package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void getTasks_NoTask_EmptyList() {
        assertThat(taskService.getTasks()).isEmpty();
    }

    @Test
    void getTasks_Task_TaskInList() {
        Task task = new Task();
        task.setTitle("test");
        Task generatedTask = taskService.createTask(task);

        assertThat(taskService.getTasks()).anySatisfy(t -> {
            assertThat(t.getTitle()).isEqualTo("test");
            assertThat(t.getId()).isEqualTo(generatedTask.getId());
        });
    }

    @Test
    void getTask_NonExistingId_ThrowTaskNotFoundException() {
        assertThatThrownBy(() -> taskService.getTask(1L)).hasMessageStartingWith("Task not found:");
    }

    @Test
    void getTask_ExistingId_ReturnTask() {
        Task task = new Task();
        task.setTitle("test");
        Task generatedTask = taskService.createTask(task);
        Long generatedId = generatedTask.getId();

        assertThat(taskService.getTask(generatedId).getId()).isEqualTo(generatedId);
        assertThat(taskService.getTask(generatedId).getTitle()).isEqualTo(generatedTask.getTitle());
    }

    @Test
    void createTask_Will_ReturnCreatedTask() {
        Task task = new Task();
        task.setTitle("test");
        Task generatedTask = taskService.createTask(task);


        assertThat(generatedTask.getId()).isEqualTo(taskService.getTask(generatedTask.getId()).getId());
        assertThat(generatedTask.getTitle()).isEqualTo("test");
    }

    @Test
    void createTask_Will_AddTaskInList() {
        Task task = new Task();
        task.setTitle("test");
        Task generatedTask = taskService.createTask(task);
        Long generatedId = generatedTask.getId();

        assertThat(taskService.getTask(generatedId)).isEqualTo(generatedTask);
    }

    @Test
    void updateTask_ExisingId_ChangeTaskTitle() {
        Task task = new Task();
        task.setTitle("test");
        Task firstTask = taskService.createTask(task);

        Task newTask = new Task();
        newTask.setTitle("new");
        Task updatedTask = taskService.updateTask(firstTask.getId(), newTask);

        assertThat(updatedTask.getTitle()).isEqualTo("new");
        assertThat(updatedTask.getId()).isEqualTo(firstTask.getId());
    }

    @Test
    void deleteTask_ExisingId_ReturnTargetTask() {
        Task task = new Task();
        task.setTitle("test");
        Task firstTask = taskService.createTask(task);

        assertThat(taskService.deleteTask(firstTask.getId())).isEqualTo(firstTask);
    }

    @Test
    void deleteTask_ExisingId_DeleteTaskFromList() {
        Task task = new Task();
        task.setTitle("test");
        Task firstTask = taskService.createTask(task);

        taskService.deleteTask(firstTask.getId());
        assertThat(taskService.getTasks()).doesNotContain(firstTask);
    }
}