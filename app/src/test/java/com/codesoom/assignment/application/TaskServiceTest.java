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
    private static final String TASK_TITLE = "test";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void getTasks() {
        //given
        Task task1 = new Task();
        Task task2 = new Task();

        taskService.createTask(task1);
        taskService.createTask(task2);

        // when
        List<Task> tasks = taskService.getTasks();

        //then
        assertThat(tasks.size()).isEqualTo(2);
        assertThat(tasks).contains(task1, task2);
    }

    @Test
    void getTask() {
        //given
        Task task = new Task();
        taskService.createTask(task);

        //when
        Task foundTask = taskService.getTask(1L);

        //then
        assertThat(foundTask).isEqualTo(task);
    }

    @Test
    void createTask() {
        //given
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        //when
        Task createdTask = taskService.createTask(task);

        //then
        Task foundItem = taskService.getTask(createdTask.getId());
        assertThat(foundItem).isEqualTo(createdTask);
    }

    @Test
    void updateTask() {
        //given
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        Task createdTask = taskService.createTask(task);
        Long itemId = createdTask.getId();

        //when
        Task updateTask = new Task();
        updateTask.setTitle("update title");
        taskService.updateTask(itemId, updateTask);

        //then
        Task foundItem = taskService.getTask(itemId);
        assertThat(foundItem.getTitle()).isEqualTo(updateTask.getTitle());
    }

    @Test
    void deleteTask() {
        //given
        Task task = new Task();
        Task createdTask = taskService.createTask(task);

        //when
        taskService.deleteTask(createdTask.getId());

        //then
        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).doesNotContain(task);
        assertThatThrownBy(() -> taskService.getTask(createdTask.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
