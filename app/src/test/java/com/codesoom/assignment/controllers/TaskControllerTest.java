package com.codesoom.assignment.controllers;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Task Controller는")
class TaskControllerTest {

    private TaskController controller;

    public static final long findId = 1L;

    @BeforeEach
    void setUp() {
        TaskRepository taskRepository = new TaskRepository();
        TaskService taskService = new TaskService(taskRepository);
        controller = new TaskController(taskService);
        controller.createTask(new Task(findId, "title"));
    }

    @Nested
    @DisplayName("전체 할 일 리스트에 대한 조회 요청을 했을 때")
    class GetTaskList {
        @Test
        @DisplayName("할 일이 없으면 빈 리스트를 리턴한다.")
        void getTasksWithoutTask() {
            controller.completeTask(findId);
            assertThat(controller.getTaskList()).isEmpty();
        }

        @Test
        @DisplayName("할 일이 1개 이상이면 할 일 리스트를 리턴한다.")
        void getTasks() {
            assertThat(controller.getTaskList().size()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("할 일(id)에 대한 조회 요청을 했을 때")
    class GetTask {
        @Test
        @DisplayName("할 일(id)을 찾지 못했다면 TaskNotFound 예외를 던진다.")
        void getTasksWithoutTask() {
            assertThatThrownBy(() -> {
                Task task = controller.getTask(9999L);
            }).isInstanceOf(TaskNotFoundException.class);
        }

        @Test
        @DisplayName("할 일(id)을 찾으면 할 일을 리턴한다.")
        void getTask() {
            controller.getTask(findId);
            assertThat(controller.getTask(findId).getTitle()).isEqualTo("title");
        }
    }

    @Nested
    @DisplayName("할 일에 대한 등록 요청이 오면")
    class CreateTask {
        @Test
        @DisplayName("할 일을 등록한다.")
        void createNewTask() {

            int oldSize = controller.getTaskList().size();
            controller.createTask(new Task(2L, "book"));

            assertThat(controller.getTaskList()).isNotEmpty();
            assertThat(controller.getTaskList().size() - oldSize).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("할 일(id)에 대한 수정 요청이 오면")
    class UpdateTask {

        @Test
        @DisplayName("할 일(id)을 수정한다.")
        void updateTask() {
            String newTitle = "New Title";
            Task newTask = new Task(findId, newTitle);
            controller.updateTask(findId, newTask);

            assertThat(controller.getTask(findId).getTitle()).isEqualTo(newTitle);
            assertThat(controller.getTask(findId).getId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("할 일(id)에 대한 완료 요청이 오면")
    class CompleteTask {

        @Test
        @DisplayName("할 일(id)을 리스트에서 제외한다.")
        void completeTask() {

            controller.completeTask(findId);
            assertThatThrownBy(() -> {
                Task task = controller.getTask(findId);
            }).isInstanceOf(TaskNotFoundException.class);

        }
    }

    @Test
    void taskToString() {
        Task task = controller.getTask(findId);
        Assertions.assertThat(task.toString()).isEqualTo("Task{id=1, title=title}");
    }

}