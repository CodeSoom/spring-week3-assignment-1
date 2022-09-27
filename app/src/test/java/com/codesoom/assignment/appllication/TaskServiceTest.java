package com.codesoom.assignment.appllication;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private static final String TASK_TITLE="test";
    private static final String UPDATE_POSTFIX="update";
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        //subject
        taskService = new TaskService();

        //fixtures
        Task task = new Task(1L, TASK_TITLE);

        taskService.createTask(task);
    }

    @Test
    void getTaskWithValidId() {
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(2L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.updateTitle(TASK_TITLE);

        taskService.createTask(task);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    void deleteTask() {
        int oldSize = taskService.getTasks().size();

       taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(-1);
    }

    @Test
    void taskList() {
        assertThat(taskService.getTasks().size()).isEqualTo(1);
    }

    @Test
    void updateTask() {
        System.out.println(taskService.getTask(1L).getTitle());
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TASK_TITLE);

        Task source = new Task(1L,UPDATE_POSTFIX);
        Task task = taskService.updateTask(1L,source);

        assertThat(task.getTitle()).isEqualTo(UPDATE_POSTFIX);

    }

}