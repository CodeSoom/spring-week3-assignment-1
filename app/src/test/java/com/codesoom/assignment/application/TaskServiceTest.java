package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class TaskServiceTest {
    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskService = new TaskService();
    }

    @Test
    @DisplayName("check getTasks returns a list")
    void getTasks() {
        assertThat(taskService.getTasks()).hasSize(0);
    }
    @Test
    @DisplayName("check if created task matches with found task")
    void getTaskWithValidId(){
        //given
        Task task = taskService.createTask(new Task(1L, "title"));
        //when
        Task getTask= taskService.getTask(1L);
        //then
        assertThat(getTask.getId()).isEqualTo(task.getId());
        assertThat(getTask.getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    @DisplayName("throw TaskNotFoundException if getTask is not found in the database")
    void getIdWithInvalidId(){
        //given
        taskService.createTask(new Task(1L, "title"));
        //when
        Throwable thrown = catchThrowable(() -> taskService.getTask(2L));
        //then
        assertThat(thrown).isInstanceOf(TaskNotFoundException.class);
    }


    @Test
    @DisplayName("check if created task matches with the newly created task")
    void createTaskWithValidInput() {
        //given
        Task task = new Task(1L, "title");
        //when
        Task createdTask = taskService.createTask(task);
        //then
        assertThat(createdTask.getTitle()).isEqualTo(task.getTitle());
        assertThat(createdTask.getId()).isEqualTo(task.getId());

    }

    @Test
    @DisplayName("check if updated task matches with a previously created task")
    void updateTaskWithValidId() {
        //given
        taskService.createTask(new Task(1L, "title"));

        //when
        Task newTask = new Task(1L,"updated task");
        Task updatedTask = taskService.updateTask(1L, newTask);
        //then
        assertThat(newTask.getTitle()).isEqualTo(updatedTask.getTitle());
        assertThat(newTask.getId()).isEqualTo(updatedTask.getId());
    }

    @Test
    void updateTaskWithInvalidId(){
        //given
        taskService.createTask(new Task(1L, "title"));

        //when
        Task newTask = new Task(1L,"updated task");
        Task updatedTask = taskService.updateTask(1L, newTask);
        //then
        assertThat(newTask.getTitle()).isEqualTo(updatedTask.getTitle());
        assertThat(newTask.getId()).isEqualTo(updatedTask.getId());
    }

    @Test
    void deleteTaskWithValidId() {
        //given
        Task createdTask = taskService.createTask(new Task(1L, "title"));
        //when
        Task deletedTask = taskService.deleteTask(1L);
        //then
        assertThat(createdTask.getId()).isEqualTo(deletedTask.getId());
        assertThat(createdTask.getTitle()).isEqualTo(deletedTask.getTitle());
    }

    @Test
    void deleteTaskWithInvalidId() {
        //given
        taskService.createTask(new Task(1L, "title"));
        //when
        Throwable throwable = catchThrowable(() -> taskService.deleteTask(2L));
        //then
        assertThat(throwable).isInstanceOf(TaskNotFoundException.class);
    }
}
