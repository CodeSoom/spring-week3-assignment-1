package com.codesoom.assignment.service;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TaskServiceTest {

    private TaskService taskService;

    static final String TASK_TITLE = "New Task";
    static final String TASK_OLD_TITLE = "Old Task";

    @BeforeEach
    void init() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @DisplayName("Task 조회 테스트")
    @Nested
    class getTaskProcess {
        @Test
        public void getTasksValid() {
            assertThat(taskService.getTasks()).isNotEmpty();
        }

        @Test
        public void getTaskValid() {
            //given
            Task task = new Task();
            task.setTitle("일1");
            //when
            Task task1 = taskService.createTask(task);
            Task makeService = taskService.getTask(task1.getId());
            //Then
            assertThat(makeService.getTitle()).isEqualTo("일1");
        }

        @Test
        public void getTaskInvalid() {

            assertThat(taskService.getTasks().get(0).getTitle()).isEqualTo(TASK_TITLE);

            assertThatThrownBy(() -> taskService.getTask(10L))
                    .isInstanceOf(TaskNotFoundException.class);
        }
    }

    @DisplayName("Task 수정")
    @Nested
    class UpdateTaskProcess {

        @Test
        public void updateTask() {
            //given
            Task source = new Task();
            source.setTitle("New Title");
            taskService.updateTask(1L, source);
            //when
            Task task = taskService.getTask(1L);
            System.out.println("UpdateTaskName:{} " + task.getTitle());
            //Then
            assertThat(task.getTitle()).isNotEqualTo(TASK_TITLE);
        }

    }

    @DisplayName("Task 생성")
    @Nested
    class CreateTaskProcess {
        @Test
        public void createTask() {
            int oldSize = taskService.getTasks().size();

            Task task = new Task();
            task.setTitle(TASK_TITLE);

            taskService.createTask(task);

            int newSize = taskService.getTasks().size();
            assertThat(newSize - oldSize).isEqualTo(1);
        }
    }

    @DisplayName("Task 수정")
    @Nested
    class UpdateTask {

        @Test
        public void updateValid() throws Exception {
            //given
            Task task = new Task();
            task.setTitle(TASK_OLD_TITLE);
            //when
            taskService.updateTask(1L, task);
            //Then
            assertThat(task.getTitle()).isEqualTo(TASK_OLD_TITLE);
        }

        @Test
        public void updateInValid() throws Exception {
            Task task = new Task();
            task.setTitle(TASK_OLD_TITLE);
            assertThatExceptionOfType(TaskNotFoundException.class)
                    .isThrownBy(() -> taskService.updateTask(100L, task))
                    .withMessageNotContainingAny("Task not found 100");
        }

    }

    @DisplayName("Task 삭제")
    @Nested
    class DeleteTask {
        @DisplayName("Task 삭제 테스트")
        @Test
        public void deleteTask() {
            //given
            int oldSize = taskService.getTasks().size();
            //when
            taskService.deleteTask(1L);
            int newSize = taskService.getTasks().size();
            //Then
            assertThat(oldSize - newSize).isEqualTo(1);
        }

        @Test
        public void deleteInValid() throws Exception {
            //given
            List<Task> list = taskService.getTasks();
            //when
            assertThatExceptionOfType(TaskNotFoundException.class)
                    .isThrownBy(() -> {
                        taskService.deleteTask(100L);
                    }).withMessageNotContainingAny("Task not found 100");
        }

    }


}