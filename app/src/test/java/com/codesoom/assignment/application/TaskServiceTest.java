package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    void updateTask(){
        Task source = new Task();
        source.setTitle(TASK_TITLE + "!!");
        taskService.updateTask(1L, source);
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + "!!");
    }

    @Test
    void deleteTask(){
        int oldSize = taskService.getTasks().size();
        taskService.deleteTask(1L);
        int newSize = taskService.getTasks().size();
        assertThat(oldSize - newSize).isEqualTo(1);
    }

    @Test
    void generateId() {
        TaskService taskService = new TaskService();
        try{
            Method method = taskService.getClass().getDeclaredMethod("generateId");
            method.setAccessible(true);
            Long ref = (Long) method.invoke(taskService);
            assertThat(ref).isEqualTo(1L);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
