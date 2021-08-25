package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskController 클래스")
class TaskControllerTest {
    TaskController controller;
    TaskService taskService = new TaskService();
    private static final Long NOT_EXIST_ID = 999999999L;
    private static final String TASK_TITLE = "test";

    @BeforeEach
    void setUp() {
        controller = new TaskController(taskService);
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("등록된 할 일이 없다면")
        class Context_no_have_tasks {
            @Test
            @DisplayName("비어있는 리스트를 리턴합니다")
            void it_returns_emptyTasks() {
                final List<Task> list = controller.list();
                assertThat(list).isEmpty();
            }
        }

        @Nested
        @DisplayName("등록된 할 일이 있다면")
        class Context_have_tasks {
            Task task1, task2;

            @BeforeEach
            void prepareTasks() {
                task1 = new Task();
                task2 = new Task();
                taskService.createTask(task1);
                taskService.createTask(task2);
            }

            @Test
            @DisplayName("할 일들을 리턴합니다")
            void it_returns_tasks() {
                // when
                final List<Task> list = controller.list();

                //then
                assertThat(list.size()).isEqualTo(2);
                assertThat(list).contains(task1, task2);
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 없다면")
        class Context_matchId_NotExist {
            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던집니다")
            void it_returns_TaskNotFoundException() {
                assertThatThrownBy(() -> controller.detail(NOT_EXIST_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 있다면")
        class Context_matchId_exist {
            Task task;
            Long id;

            @BeforeEach
            void prepareTask() {
                task = new Task();
                Task createdTask = taskService.createTask(this.task);
                id = createdTask.getId();
            }

            @Test
            @DisplayName("할 일을 리턴합니다")
            void it_returns_task() {
                //when
                Task foundTask = controller.detail(id);

                //then
                assertThat(foundTask).isEqualTo(task);
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("할 일을 생성했다면")
        class Context_create_a_task {
            Task task;

            @BeforeEach
            void prepareTask() {
                task = new Task();
            }

            @Test
            @DisplayName("생성한 할 일을 반환합니다")
            void createTask() {
                //when
                Task createdTask = controller.create(task);

                //then
                Task foundItem = taskService.getTask(createdTask.getId());
                assertThat(foundItem).isEqualTo(createdTask);
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 있다면")
        class Context_matchId_exist {
            Task task, updateTask;
            Long itemId;

            @BeforeEach
            void prepareTask() {
                task = new Task();
                task.setTitle(TASK_TITLE);

                Task createdTask = taskService.createTask(task);
                itemId = createdTask.getId();

                updateTask = new Task();
                updateTask.setTitle("update title");
            }

            @Test
            @DisplayName("입력받은 제목으로 수정된 할 일을 리턴합니다")
            void updateTask() {
                //when
                controller.update(itemId, updateTask);

                //then
                Task foundItem = taskService.getTask(itemId);
                assertThat(foundItem.getTitle()).isEqualTo(updateTask.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_patch {
        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 있다면")
        class Context_matchId_exist {
            Task task, updateTask;
            Long itemId;

            @BeforeEach
            void prepareTask() {
                task = new Task();
                task.setTitle(TASK_TITLE);

                Task createdTask = taskService.createTask(task);
                itemId = createdTask.getId();

                updateTask = new Task();
                updateTask.setTitle("update title");
            }

            @Test
            @DisplayName("입력받은 제목으로 수정된 할 일을 리턴 합니다")
            void updateTask() {
                //when
                controller.patch(itemId, updateTask);

                //then
                Task foundItem = taskService.getTask(itemId);
                assertThat(foundItem.getTitle()).isEqualTo(updateTask.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        @Nested
        @DisplayName("입력받은 id와 일치하는 등록된 할 일이 있다면")
        class Context_matchId_exist {
            Task task, createdTask;

            @BeforeEach
            void prepareTask() {
                task = new Task();
                createdTask = taskService.createTask(task);
            }

            @Test
            @DisplayName("할 일을 삭제합니다")
            void deleteTask() {
                //when
                controller.delete(createdTask.getId());

                //then
                final List<Task> tasks = taskService.getTasks();
                assertThat(tasks).doesNotContain(task);
            }
        }
    }
}
