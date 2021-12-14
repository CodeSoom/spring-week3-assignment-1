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
    private static final String UPDATE_POSTFIX = "New";

    @BeforeEach
    void setUp() {
        // subject
        taskService = new TaskService();

        //fixture
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Test
    void getTasksList(){
        List<Task> tasks = taskService.getTasks();
        assertThat(tasks).hasSize(1);

        Task task = tasks.get(0);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithValidId(){
        Task found = taskService.getTask(1L);

        assertThat(found.getTitle()).isEqualTo(TASK_TITLE);
    }
    @Test
    void getTaskWithInvalidId(){
        assertThatThrownBy(()->taskService.getTask(2L))
                .isInstanceOf(TaskNotFoundException.class);
    }
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

        assertThat(newSize - oldSize).isEqualTo(0);
    }

    @Test
    void updateTask(){
        Task source = new Task();
        source.setTitle(UPDATE_POSTFIX + TASK_TITLE);
        taskService.updateTask(1L,source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(UPDATE_POSTFIX + TASK_TITLE);
    }
}
