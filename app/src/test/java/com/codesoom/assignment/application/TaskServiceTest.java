package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskServiceTest {

    TaskService taskService;

    @BeforeEach
    void setup() {
        taskService = new TaskService();
    }

    private Task createSampleTask() {
        Task task = new Task();
        task.setTitle("old title");
        Task createdTask = taskService.createTask(task);
        return createdTask;
    }

    @Test
    @DisplayName("getTasks() - task 존재할때")
    void getTasks() {
        createSampleTask();

        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(1);
    }

    @Test
    @DisplayName("getTasks() - task 없을때")
    void getTasksNotIn() {
        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).isEmpty();
        assertThat(tasks).hasSize(0);
    }

    @Test
    @DisplayName("getTask() - id 에 해당하는 task 반환")
    void getTask() {
        Task createTask = createSampleTask();

        Task getTask = taskService.getTask(1L);

        assertThat(getTask.getId()).isEqualTo(createTask.getId());
        assertThat(getTask.getTitle()).isEqualTo(createTask.getTitle());
        assertThat(getTask.getClass()).isEqualTo(createTask.getClass());
    }

    @Test
    @DisplayName("getTask() - id 에 해당하는 task 없음")
    void getTaskNotIn() {
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(3L));
    }

    @Test
    @DisplayName("createTask()")
    void createTask() {
        Task task = new Task();
        task.setTitle("타이틀1");
        taskService.createTask(task);
        assertEquals(taskService.getTasks().size(), 1);

        Task task2 = new Task();
        task.setTitle("타이틀2");
        taskService.createTask(task2);
        assertEquals(taskService.getTasks().size(), 2);
    }

    @Test
    @DisplayName("updateTask() - 바꿀 task와 존재하는 id 제공")
    void updateTask() {
        createSampleTask();
        Task updateTask = new Task();
        updateTask.setTitle("new title");

        taskService.updateTask(1L, updateTask);

        assertThat(taskService.getTask(1L).getTitle()).isEqualTo("new title");
    }

    @Test
    @DisplayName("updateTask() - 존재하지 않는 id 변경요청 실패")
    void updateTaskNotIn() {
        Task updateTask = new Task();
        updateTask.setTitle("new title");

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(3L, updateTask));
    }

    @Test
    @DisplayName("deleteTask() - 존재하는 id 삭제요청 성공")
    void deleteTask() {
        createSampleTask();
        assertEquals(taskService.getTasks().size(), 1);

        taskService.deleteTask(1L);

        assertEquals(taskService.getTasks().size(), 0);
    }

    @Test
    @DisplayName("deleteTask() - 존재하지 않는 id 삭제요청 실패")
    void deleteTaskNotIn() {
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(3L));
    }
}
