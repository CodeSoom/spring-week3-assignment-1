package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private TaskService taskService;
    private static final String TASK_TITLE = "test";
    private static final String POST_FIX = "???";


    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        //fixtures
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Test
    void getTasks() {
        List<Task> tasks = taskService.getTasks();

        assertThat(tasks).hasSize(1);

        Task task = tasks.get(0);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }


    @Test
    @DisplayName("getTask :  실할 일이 있으면 리스트에 있는 title과 이름이 같은지 검사한다")
    void getTaskWithValid(){
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }


    @Test
    @DisplayName("getTask : 할 일을 찾을 수 없으면 예외를 던진다")
    void getTaskWithInvalid(){ 
        assertThatThrownBy(() -> taskService.getTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }


    @Test
    @DisplayName("createTask : Task 생성이 이루어졌는지 확인한다")
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);

        int newSize = taskService.getTasks().size();
        assertThat( taskService.createTask(task).getTitle()).isEqualTo(TASK_TITLE);
        assertThat(newSize - oldSize).isEqualTo(1);
    }


    @Test
    @DisplayName("deleteTask : 존재하는 task의 id가 주어진다면 task를 삭제 한다")
    void deleteTask() {
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }


    @Test
    @DisplayName("deleteTask : 존재하지 않는 task의 id가 주어진다면 예외를 던진다")
    void deleteTaskWithInvalid() {
        assertThatThrownBy(() -> taskService.deleteTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }


    @Test
    @DisplayName("updateTask : 존재하는 task의 id가 주어진다면 task title을 update 한다")
    void updateTask() {
        Task source = new Task();
        source.setTitle(TASK_TITLE+POST_FIX);

        taskService.updateTask(1L, source);

        Task task= taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + POST_FIX);

    }

    @Test
    @DisplayName("updateTask : 존재하지 않는 task의 id가 주어진다면 예외를 던진다")
    void updateTaskWithInvalid() {
        Task source = new Task();
        assertThatThrownBy(() -> taskService.updateTask(100L, source))
                .isInstanceOf(TaskNotFoundException.class);
    }


}