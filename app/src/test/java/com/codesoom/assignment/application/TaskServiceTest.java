package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.convert.DataSizeUnit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }


    @Nested
    @DisplayName("getTasks")
    class testGetTasks{
        @Test
        @DisplayName("태스크가 존재하지 않을 경우, 비어있는 리스트를 반환한다")
        public void getTasks_NoTask_EmptyList() {
            assertThat(taskService.getTasks()).isEmpty();
        }

        @Test
        @DisplayName("태스크가 존재할 경우, 존재하는 태스크를 포함한 리스트를 반환한다.")
        void getTasks_Task_TaskInList() {
            // given
            Task task = new Task();
            task.setTitle("test");
            Task generatedTask = taskService.createTask(task);

            // when
            List<Task> receipt = taskService.getTasks();

            // then
            assertThat(receipt).anySatisfy(t -> {
                assertThat(t.getTitle()).isEqualTo("test");
                assertThat(t.getId()).isEqualTo(generatedTask.getId());
            });
        }


    }


    @Nested
    @DisplayName("getTask")
    class testGetTask{
        @Test
        @DisplayName("존재하지 않는 테스크 id 를 인자로 받으면, 익셉션을 던진다")
        void getTask_NonExistingId_ThrowTaskNotFoundException() {
            assertThatThrownBy(() -> taskService.getTask(1L)).hasMessageStartingWith("Task not found:");
        }

        @Test
        @DisplayName("동록되어있는 태스크의 id 를 인자로 받으면, 해당 태스크를 리턴한다")
        void getTask_ExistingId_ReturnTask() {
            Task task = new Task();
            task.setTitle("test");
            Task generatedTask = taskService.createTask(task);
            Long generatedId = generatedTask.getId();

            assertThat(taskService.getTask(generatedId).getId()).isEqualTo(generatedId);
            assertThat(taskService.getTask(generatedId).getTitle()).isEqualTo(generatedTask.getTitle());
        }


    }


    @Nested
    @DisplayName("createTask")
    class testCreatTask{
        @Test
        @DisplayName("새로운 태스크를 만든다면, 만들어진 태스크를 반환한다.")
        void createTask_Will_ReturnCreatedTask() {
            // given
            Task task = new Task();
            task.setTitle("test");

            // when
            Task generatedTask = taskService.createTask(task);

            // then
            assertThat(generatedTask.getId()).isEqualTo(taskService.getTask(generatedTask.getId()).getId());
            assertThat(generatedTask.getTitle()).isEqualTo("test");
        }

        @Test
        @DisplayName("새로운 태스크를 만든다면, 리스트에 해당 태스크를 등록한다.")
        void createTask_Will_AddTaskInList() {
            // given
            Task task = new Task();
            task.setTitle("test");

            // when
            Task generatedTask = taskService.createTask(task);

            // then
            Long generatedId = generatedTask.getId();
            assertThat(taskService.getTask(generatedId)).isEqualTo(generatedTask);
        }

    }


    @Nested
    @DisplayName("updateTask")
    class testUpdateTask{
        @Test
        @DisplayName("등록된 태스크가 있을때, 존재하는 id 의 태스크를 변경하면, 새로운 타이틀로 변경한다.")
        void updateTask_ExisingId_ChangeTaskTitle() {
            // given
            Task task = new Task();
            task.setTitle("test");
            Task firstTask = taskService.createTask(task);

            // when
            Task newTask = new Task();
            newTask.setTitle("new");
            Task updatedTask = taskService.updateTask(firstTask.getId(), newTask);

            // then
            assertThat(updatedTask.getTitle()).isEqualTo("new");
            assertThat(updatedTask.getId()).isEqualTo(firstTask.getId());
        }
    }

    @Nested
    @DisplayName("deleteTask")
    class testDeleteTask{
        @Test
        @DisplayName("등록된 태스크가 존재할때, 해당 id 로 태스크를 지우면, 지워진 태스크를 리턴한다.")
        void deleteTask_ExisingId_ReturnTargetTask() {
            Task task = new Task();
            task.setTitle("test");
            Task firstTask = taskService.createTask(task);

            assertThat(taskService.deleteTask(firstTask.getId())).isEqualTo(firstTask);
        }

        @Test
        @DisplayName("등록된 태스크가 존재할때, 해당 id로 태스크를 지우면, 서비스의 리스트에서 삭제된다")
        void deleteTask_ExisingId_DeleteTaskFromList() {
            Task task = new Task();
            task.setTitle("test");
            Task firstTask = taskService.createTask(task);

            taskService.deleteTask(firstTask.getId());
            assertThat(taskService.getTasks()).doesNotContain(firstTask);
        }
    }







}
