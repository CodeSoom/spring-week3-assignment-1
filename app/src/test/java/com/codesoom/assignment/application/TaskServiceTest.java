package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void getEmptyTasks() {
        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).isEmpty();
    }

    @Test
    void getTaskWithInvalidId() {
        long invalid_id = 42L;
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTask(invalid_id));
    }

    @Test
    void createTask() {
        // given
        Task source = new Task();
        source.setId(1L);
        source.setTitle("task1");

        // when
        Task task = taskService.createTask(source);

        // then
        assertThat(task).isEqualTo(source);
    }

    @Test
    void getTask() {
        // given
        Task source = new Task();
        source.setId(1L);
        source.setTitle("task1");

        // TODO: 음.. 뭔가 createTask를 테스트한 직후에 여기서 테스트하는게 괜찮은 건가..?
        //       설계가 이상한 것 같기도 하구
        taskService.createTask(source);

        // when
        Task task = taskService.getTask(source.getId());

        // then
        assertThat(task).isEqualTo(source);
    }

    @Test
    void updateTask() {
        // given
        Task source = new Task();
        source.setId(1L);
        source.setTitle("task1");

        taskService.createTask(source);

        // when
        source.setTitle("modifedTitle");
        Task task = taskService.updateTask(source.getId(), source);

        // then
        assertThat(task).isEqualTo(source);
    }

    @Test
    void deleteTask() {
        // given
        Task source = new Task();
        source.setId(1L);
        source.setTitle("task1");

        taskService.createTask(source);

        // when
        taskService.deleteTask(source.getId());

        // then
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTask(source.getId()));
    }
}