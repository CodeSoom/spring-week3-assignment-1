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
import static org.assertj.core.api.ThrowableAssert.catchThrowable;


@DisplayName("TestController")
class TaskControllerTest {
    private TaskController taskController;
    private TaskService taskService;

    private Task task;
    private List<Task> tasks;

    @BeforeEach
    public void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    public Task makeTask(String title) {
        Task task = new Task();
        task.setTitle(title);
        task = taskService.createTask(task);
        return task;
    }

    public Task findTaskById(List<Task> tasks, Long taskId) {
        return tasks
                .stream()
                .filter(x -> x.getId()
                        .equals(taskId))
                .findFirst()
                .orElse(null);
    }


    @Nested
    @DisplayName("list() 메서드는")
    class Describe_list {
        @Nested
        @DisplayName("할 일에 대한 목록이 존재한다면")
        class Context_valid_not_empty_list {
            @Test
            @DisplayName("Null이 아니고 비어 있지 않은 목록을 반환한다.")
            void list_valid_not_emtpy() {
                //given
                Task task = makeTask("New Title1");
                //when
                List<Task> list = taskController.list();
                //then
                assertThat(list)
                        .as("Task List NotNull Test")
                        .isNotNull()
                        .isNotEmpty();
            }

            @Test
            @DisplayName("생성된 할 일의 수 만큼의 목록을 반환한다.")
            void list_valid_return() {
                //given
                int listSize = 5;
                for (int i = 0; i < listSize; i++) {
                    Task task2 = makeTask("New Title" + i);
                }
                //when
                List<Task> list = taskController.list();

                //then
                assertThat(list)
                        .hasSize(listSize);
            }
        }

        @Nested
        @DisplayName("할 일의 목록이 비어있다면")
        class Context_invalid_empty_list {
            @Test
            @DisplayName("빈 목록을 반환한다.")
            void list_valid_empty() {
                //given
                //when
                List<Task> list = taskController.list();
                //then
                assertThat(list)
                        .isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("detail(id) 메서드는")
    class Describe_detail {
        @Nested
        @DisplayName("요청한 할 일이 목록에 있다면")
        class Context_valid_detail {
            private Long taskId;
            private Task returnTask;

            @BeforeEach
            void setUpValidDetail() {
                //given
                task = makeTask("New Task");
                taskId = task.getId();
                //when
                tasks = taskController.list();
                returnTask = findTaskById(tasks, taskId);
            }

            @Test
            @DisplayName("리턴 값은 null이 아니다.")
            void detail_with_valid_not_null() {
                //then
                assertThat(returnTask)
                        .isNotNull();
            }

            @Test
            @DisplayName("요청한 할일을 반환한다.")
            void detail_with_valid() {
                //then
                assertThat(returnTask.getTitle())
                        .isEqualTo(task.getTitle());
                assertThat(returnTask.getId())
                        .isEqualTo(task.getId());
            }
        }

        @Nested
        @DisplayName("요청한 할일이 목록에 없다면")
        class Context_invalid_detail {
            private Long taskId = 1L;
            private String errorMessage = String.format("Task not found: %d", taskId);

            @Test
            @DisplayName("TaskNotFoundException 발생")
            void detail_invalid_exception() {
                //when
                Throwable thrown = catchThrowable(() -> {
                    taskController.detail(taskId);
                });
                //then
                assertThat(thrown)
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(errorMessage);
            }
        }
    }


    @Nested
    @DisplayName("create(Task) 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("할 일이 정상적으로 등록되면")
        class Context_create_valid {
            private String title;
            private Task returnTask;

            @BeforeEach
            void setUpValidCreate() {
                //given
                title = "Second Title";
                task = new Task();
                task.setTitle(title);
                //when
                returnTask = taskController.create(task);
            }

            @Test
            @DisplayName("요청한 할 일과 동일한 할 일을 반환한다. ")
            void create_valid_task() {
                //then
                assertThat(returnTask)
                        .extracting("title")
                        .isEqualTo(title);
            }
        }
    }

    @Nested
    @DisplayName("update(id) 메소드는")
    class Describe_update {
        @Nested
        @DisplayName("목록에 있는 Task를 수정한다면")
        class Context_valid_update_task {
            private Long taskId;

            @BeforeEach
            void setUpValidUpdate() {
                //given
                task = makeTask("First Title");
                taskId = task.getId();
                task = taskController.detail(taskId);
            }

            @Test
            @DisplayName("목록의 Task 정보를 수정한다")
            void update_valid_update_task() {
                //given
                String preTitle = task.getTitle();
                Task newTask = makeTask("Second Title");

                //when
                Task returnTask = taskController.update(taskId, newTask);

                //then
                assertThat(returnTask)
                        .extracting("title")
                        .isNotEqualTo(preTitle);
            }

            @Test
            @DisplayName("목록의 할 일을 수정 후 반환한다.")
            void update_valid_update_task_return() {
                //when
                Task returnTask = taskController.update(taskId, task);

                //then
                assertThat(returnTask)
                        .extracting("title")
                        .isEqualTo(task.getTitle());
            }
        }

        @Nested
        @DisplayName("목록에 없는 Task를 수정한다면")
        class Context_invalid_update_task {
            private Long taskId;
            private String errorMessage;

            @BeforeEach
            void setUpInvalidUpdate() {
                //given
                Task task = makeTask("First Title");
                taskId = task.getId() + 1L;

            }

            @Test
            @DisplayName("TaskNotFoundException을 발생한다.")
            void invalid_update_task_exception() {
                errorMessage = "Task not found: " + taskId;

                //when
                Throwable thrown = catchThrowable(() -> {
                    taskController.update(taskId, task);
                });

                //then
                assertThat(thrown)
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(errorMessage);

            }
        }
    }


    @Nested
    @DisplayName("delete(id) 메소드는")
    class Describe_delete {
        @Nested
        @DisplayName("목록에 있는 Task 를 지우면")
        class Context_valid_delete {
            @Test
            @DisplayName("요청한 Task를 삭제한다.")
            void delete_valid_task() {
                //given
                Task task = makeTask("First Title");
                Long taskId = task.getId();
                Task preTask = taskController.detail(taskId);

                //when
                taskService.deleteTask(taskId);
                List<Task> tasks = taskController.list();
                Task returnTask = findTaskById(tasks, taskId);

                //then
                assertThat(returnTask).isNull();
            }
        }

        @Nested
        @DisplayName("목록에 없는 Task 를 지우면")
        class Context_invalid_delete {
            @Test
            @DisplayName("TaskNotFoundException을 발생한다.")
            void invalid_delete_task_exception() {
                //given
                Task task = makeTask("First Title");
                Long taskId = task.getId() + 1L;
                String errorMessage = "Task not found: " + taskId;

                //when
                Throwable thrown = catchThrowable(() -> {
                    taskController.delete(taskId);
                });

                //then
                assertThat(thrown)
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(errorMessage);
            }
        }
    }
}
