package com.codesoom.assignment.service;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskServiceTest {

    private TaskService taskService;

    static final String TASK_TITLE = "test";

    @BeforeEach
    void init() throws Exception {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @DisplayName("Task 조회 테스트")
    @Nested
    class getTaskProcess {
        @DisplayName("getTasks -> 전체 리스트 조회")
        @Test
        public void getTasksValid() throws Exception {
            assertThat(taskService.getTasks()).isNotEmpty();
        }

        @DisplayName("getTask-> 단일 리스트 조회")
        @Test
        public void getTaskValid() throws Exception {
            //given
            Task task = new Task();
            task.setTitle("일1");
            //when
            Task task1 = taskService.createTask(task);
            Task makeService = taskService.getTask(task1.getId());
            //Then
            assertThat(makeService.getTitle()).isEqualTo("일1");
        }

        @DisplayName("getTask-> 단일 리스트 조회 (오류)")
        @Test
        public void getTaskInvalid() {

            assertThat(taskService.getTasks().get(0).getTitle()).isEqualTo(TASK_TITLE);

            assertThatThrownBy(() -> taskService.getTask(10L))
                    .isInstanceOf(TaskNotFoundException.class);
        }
    }

    @Nested
    class CreateTaskProcess{

        @Test
        public void createTask() throws Exception{
            //given
            Task task = new Task();
            task.setTitle("일");
            //when
            taskService.createTask(task);

            int size = taskService.getTasks().size();
            //Then
            assertThat(size).isEqualTo(2);
            assertThat(size).isNotNull();
        }


    }

}