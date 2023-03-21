package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.StatusAssertions;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


class TaskServiceTest {

    private TaskService taskService;
    private static final String TITLE = "TEST_TITLE";

    private static final String UPDATE_TITLE = "UPDATE_TITLE";

    private static final Long DEFAULT_ID = 1L;

    private static final Long CREATE_ID = 2L;

    private static final Long INVALID_ID = 100L;

    @Nested
    @DisplayName("Task 리스트 테스트")
    class TasksTest{
        @BeforeEach
        void setUp(){
            Task task = new Task();
            task.setTitle(TITLE);
            taskService = new TaskService();
            taskService.createTask(task);
        }

        @Test
        @DisplayName("Task 리스트 조회")
        void getTasks() {

            assertThat(taskService.getTasks()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Task 상세 테스트")
    class TaskDetailTest{
        @BeforeEach
        void setUp(){
            Task task = new Task();
            task.setTitle(TITLE);
            taskService = new TaskService();
            taskService.createTask(task);
        }

        @Test
        @DisplayName("유효 ID를 통한 상세 조회")
        void getTaskWithValidId() {
            assertThat(taskService.getTask(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
            assertThat(taskService.getTask(DEFAULT_ID).getTitle()).isEqualTo(TITLE);
        }

        @Test
        @DisplayName("유효하지 않은 ID를 통한 상세 조회")
        void getTaskWithInvalidId() {
            // assertThatThrownBy 메서드를 통해 바로 IsInstanceOf 체인메서드를 호출하여 비교
            assertThatThrownBy(() -> taskService.getTask(INVALID_ID)).
                    isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("유효하지 않은 ID를 통한 상세 조회의 다른방법")
        void getTaskWithInvalidId2() {
            // 예외 상황을 발생시킨 후 Throwable 객체 획득
            Throwable thrown = catchThrowable(() -> {taskService.getTask(INVALID_ID);});

            // inInstanceOf 메서드를 통해 Exception 비교
            assertThat(thrown).isInstanceOf(TaskNotFoundException.class)
                    .hasMessage("Task not found: " + INVALID_ID);
        }

    }

    @Nested
    class CreateTaskTest{

        @BeforeEach
        void setUp(){
            Task task = new Task();
            task.setTitle(TITLE);
            taskService = new TaskService();
            taskService.createTask(task);
        }
        @Test
        @DisplayName("Task 생성")
        void createTask() {

            assertThat(taskService.getTasks()).hasSize(1);
            
            Task task = new Task();
            task.setTitle(TITLE);
            taskService.createTask(task);

            assertThat(taskService.getTasks()).hasSize(2);
            assertThat(taskService.getTask(CREATE_ID).getTitle()).isEqualTo(TITLE);
        }
    }

    @Nested
    class UpdateTaskTest{
        @BeforeEach
        void setUp(){
            Task task = new Task();
            task.setTitle(TITLE);
            taskService = new TaskService();
            taskService.createTask(task);
        }

        @Test
        @DisplayName("유효한 ID를 통한 수정")
        void updateTaskWithValidId() {
            Task updateTask = new Task();
            updateTask.setTitle(UPDATE_TITLE);
            taskService.updateTask(DEFAULT_ID,updateTask);

            assertThat(taskService.getTasks()).hasSize(1);
            assertThat(taskService.getTask(DEFAULT_ID).getTitle()).isEqualTo(UPDATE_TITLE);
        }

        @Test
        @DisplayName("유효하지 않는 ID를 통한 수정")
        void updateTaskWithInvalidId() {
            Task updateTask = new Task();
            updateTask.setTitle(UPDATE_TITLE);
            assertThatThrownBy(() -> taskService.updateTask(INVALID_ID,updateTask))
                    .isInstanceOf(TaskNotFoundException.class);

        }
    }

    @Nested
    class DeleteTaskTest{
        @BeforeEach
        void setUp(){
            Task task = new Task();
            task.setTitle(TITLE);
            taskService = new TaskService();
            taskService.createTask(task);
        }
        
        @Test
        @DisplayName("유효한 ID를 통한 삭제")
        void deleteTaskWithValidId() {
            Task task = new Task();
            task.setTitle(TITLE);
            taskService.createTask(task);

            assertThat(taskService.getTasks()).hasSize(2);
            taskService.deleteTask(DEFAULT_ID);
            assertThat(taskService.getTasks()).hasSize(1);
            assertThat(taskService.getTask(CREATE_ID)).isNotNull();
        }

        @Test
        @DisplayName("유효하지 않는 ID를 통한 삭제")
        void deleteTaskWithInvalidId() {
            assertThatThrownBy(() -> taskService.deleteTask(INVALID_ID))
                    .isInstanceOf(TaskNotFoundException.class);
        }
    }
    

    
}