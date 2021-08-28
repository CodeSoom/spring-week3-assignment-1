package com.codesoom.assignment.controllers;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.service.TaskCommandService;
import com.codesoom.assignment.service.TaskQueryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("New Task Controller의")
class NewTaskControllerTest {

    private NewTaskController controller;

    public static final long findId = 1L;

    @BeforeEach
    void setUp() {
        TaskRepository taskRepository = new TaskRepository();
        TaskQueryService taskQueryService = new TaskQueryService(taskRepository);
        TaskCommandService taskCommandService = new TaskCommandService(taskRepository, taskQueryService);
        controller = new NewTaskController(taskQueryService, taskCommandService);
        controller.createTask(new Task(findId, "title"));
    }

    @Nested
    @DisplayName("getTaskList 메소드는")
    class Describe_getTaskList {

        @Nested
        @DisplayName("할 일이 없으면")
        class Context_with_no_task {
            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void getTasksWithoutTask() {
                controller.completeTask(findId);
                assertThat(controller.getTaskList()).isEmpty();
            }
        }

        @Nested
        @DisplayName("할 일이 있으면")
        class Context_with_tasks {

            Task task;
            Task task2;

            @BeforeEach
            void getTasksSetUp() {
                task = controller.getTask(findId);
                controller.createTask(new Task(2L, "title2"));
                task2 = controller.getTask(2L);
            }

            @Test
            @DisplayName("할 일 리스트를 리턴한다.")
            void getTasks() {
                List<Task> taskList = controller.getTaskList();

                assertThat(taskList.size()).isEqualTo(2);
                assertThat(taskList).contains(task, task2);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {

        @Nested
        @DisplayName("id에 해당하는 할 일을 찾지 못하면")
        class Context_with_find_no_task {
            @Test
            @DisplayName("TaskNotFound 예외를 던진다.")
            void getTasksWithoutTask() {
                assertThatThrownBy(() -> {
                    Task task = controller.getTask(9999L);
                }).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("id에 해당하는 할 일을 찾으면")
        class Context_with_find_task {
            @Test
            @DisplayName("할 일을 리턴한다.")
            void getTask() {
                assertThat(controller.getTask(findId).getTitle()).isEqualTo("title");
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {

        Task newTask;
        int oldSize;

        @BeforeEach
        void createSetUp() {
            newTask = new Task(2L, "book");
            oldSize = controller.getTaskList().size();
        }

        @Test
        @DisplayName("새로운 할 일을 등록한다.")
        void createNewTask() {

            controller.createTask(newTask);
            Task findNewTask = controller.getTask(2L);

            assertThat(controller.getTaskList()).isNotEmpty();
            assertThat(controller.getTaskList().size() - oldSize).isEqualTo(1);
            assertThat(findNewTask).isNotNull();
            assertThat(findNewTask.getId()).isEqualTo(newTask.getId());
            assertThat(findNewTask.getTitle()).isEqualTo(newTask.getTitle());
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {

        String newTitle;
        Task newTask;

        @BeforeEach
        void updateSetUp() {
            newTitle = "New Title";
            newTask = new Task(findId, newTitle);
        }

        @Test
        @DisplayName("할 일을 수정한다.")
        void updateTask() {
            controller.updateTask(findId, newTask);

            assertThat(controller.getTask(findId).getTitle()).isEqualTo(newTitle);
            assertThat(controller.getTask(findId).getId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("completeTask 메소드는")
    class Describe_completeTask {
        @Test
        @DisplayName("할 일을 리스트에서 제외한다.")
        void completeTask() {
            controller.completeTask(findId);
            assertThatThrownBy(() -> {
                Task task = controller.getTask(findId);
            }).isInstanceOf(TaskNotFoundException.class);
        }
    }

    @Test
    @DisplayName("toString 메소드는 task를 출력한다.")
    void taskToString() {
        Task task = controller.getTask(findId);
        Assertions.assertThat(task.toString()).isEqualTo("Task{id=1, title=title}");
    }
}
