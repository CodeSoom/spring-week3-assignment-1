package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("할 일이 있으면 TASK_TITLE 과 이름이 같은지 검사한다")
    void getTaskWithValid(){
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }


    @Test
    @DisplayName("할 일을 찾을 수 없으면 예외를 던진다")
    void getTaskWithInvalid(){ 
        assertThatThrownBy(() -> taskService.getTask(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }


    @Test
    @DisplayName("Task 생성이 이루어졌는지 확인한다.")
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }


    @Test
    @DisplayName("Task 삭제가 이루어졌는지 확인한다")
    void deleteTask() {
        int oldSize = taskService.getTasks().size();

        Task task = new Task();

        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();

        assertThat(oldSize - newSize).isEqualTo(1);

    }

    @Test
    @DisplayName("Task name 업데이트가 이루어졌는지 확인한다")
    void updateTask() {
        Task source = new Task();
        source.setTitle(TASK_TITLE+POST_FIX);

        taskService.updateTask(1L, source);

        Task task= taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE+POST_FIX);



    }


}