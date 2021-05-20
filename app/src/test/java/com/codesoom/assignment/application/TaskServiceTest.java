package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {
    private TaskService taskService;
    String TASK_TITLE = "test";

    @BeforeEach
    void setUp() {
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
    void getTaskWitValidId() {
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWitInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
    }


//    @Test
//    void createNewTask(){
//        Task task = new Task();
//        task.setTitle("Test1");
//        controller.create(task);
//
//        task.setTitle("Test2");
//        controller.createTask(task);
//
//        assertThat(controller.list()).hasSize(1);
//        assertThat(controller.list().get(0).getId()).isEqualTo(1L); // 1: 엄격한 타입은 체크 못하는 것인지 -> float
//
//        assertThat(controller.list()).hasSize(2);
//        assertThat(controller.list().get(1).getId()).isEqualTo(1L);
//    }
//


    @Test
    void createTask(){
        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

        int newSize = taskService.getTasks().size();
        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    void deleteTask(){
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();
        assertThat(oldSize - newSize).isEqualTo(1);
    }

    @Test
    void updateTask(){
        Task source = new Task();
        source.setTitle(TASK_TITLE + "!!");
        taskService.updateTask(1L, source);
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + "!!");
    }
}