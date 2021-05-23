package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService")
class TaskServiceTest {
    private TaskService taskService;
    private List<Task> tasks;
    private Task task;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    private Task makingTask(Long index) {
        Task task = new Task();
        task.setId(index);
        task.setTitle("First Title " + index);
        return task;
    }

    @Nested
    @DisplayName("createTask(Task) 메소드는")
    class Describe_createTask {
        @Nested
        @DisplayName("할 일 등록 요청을 보내면")
        class Context_valid_create_task {
            private Long taskId = 1L;
            private Task returnTask;

            @BeforeEach
            void setUpValidCreate() {
                //given
                task = makingTask(taskId);
                //when
                returnTask = taskService.createTask(task);
            }

            @Test
            @DisplayName("할 일을 목록에 등록한다.")
            void new_create_task() {
                //then
                assertThat(returnTask)
                        .isNotNull();
            }

            @Test
            @DisplayName("할일을 등록한 뒤에 동일한 Data의 할 일을 반환한다.")
            void create_task_return_task() {
                //then
                assertThat(returnTask)
                        .extracting("id")
                        .isEqualTo(task.getId());
                assertThat(returnTask)
                        .extracting("title")
                        .isEqualTo(task.getTitle());
            }
        }
    }


    @Nested
    @DisplayName("getTasks() 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("Task 목록이 비어 있으면")
        class Context_empty_getTasks {
            private List<Task> returnTasks;

            @BeforeEach
            void setUpEmptyTasks() {
                //when
                returnTasks = taskService.getTasks();
            }

            @Test
            @DisplayName("비어 있는 목록을 반환 한다.")
            public void get_empty_tasks() {
                //then
                assertThat(returnTasks)
                        .isEmpty();
            }
        }

        @Nested
        @DisplayName("할 일 목록이 존재하면")
        class Context_not_empty_tasks {
            private Long taskIdOne = 1L;
            private Long taskIdTwo = 2L;
            private List<Task> returnTasks;

            @BeforeEach
            void setUpNotEmptyTasks() {
                //given
                tasks = new ArrayList<>();
                taskService.createTask(makingTask(taskIdOne));
                taskService.createTask(makingTask(taskIdTwo));

                //when
                returnTasks = taskService.getTasks();
            }

            @Test
            @DisplayName("비어 있지 않은 목록을 반환 한다.")
            public void get_not_empty_tasks() {
                //then
                assertThat(returnTasks).isNotEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask(id) 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("할 일이 있다면 한다면")
        class Context_valid_get_task {
            private Long taskId = 1L;
            private Task newTask;

            @BeforeEach
            void setUpValidTask() {
                //given
                task = makingTask(taskId);
                taskService.createTask(task);

                //when
                newTask = taskService.getTask(taskId);
            }

            @Test
            @DisplayName("목록에 있는 할 일을 반환 한다.")
            void get_valid_task() {
                //then
                assertThat(newTask)
                        .extracting("id").isEqualTo(taskId);
            }
        }

        @Nested
        @DisplayName("목록에 없는 할 일을 호출 한다면")
        class Context_invalid_get_task {
            private Long taskId = 2L;

            @BeforeEach
            void setUpInvalidTask() {
                //given
                task = makingTask(taskId);
            }

            @Test
            @DisplayName("TaskNotFoundException을 발생한다.")
            void get_invalid_get_task_exception() {
                //given
                String errorMessage = "Task not found: " + taskId;

                //when then
                assertThatThrownBy(() -> {
                    taskService.getTask(taskId);
                })
                        .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(errorMessage);
            }
        }
    }

    @Nested
    @DisplayName("updateTask(Task) 메서드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("목록에 있는 할일의 수정을 요청하면")
        class Context_valid_update_task {
            private Long taskId = 1L;
            private Task returnTask;

            @BeforeEach
            void setUpValidUpdate() {
                //given
                task = makingTask(taskId);
                taskService.createTask(task);
                //when
                returnTask = taskService.updateTask(taskId, task);
            }

            @Test
            @DisplayName("목록의 할 일을 수정한다.")
            void update_valid_update_task_by_id() {
                //then
                assertThat(returnTask)
                        .extracting("id")
                        .isEqualTo(task.getId());
                assertThat(returnTask)
                        .extracting("title")
                        .isEqualTo(task.getTitle());
            }
        }

        @Nested
        @DisplayName("목록에 없는 Task의 수정을 요청하면")
        class Context_invalid_delete_task {
            private Long taskId = 1L;

            @BeforeEach
            void setUpInvalidDelete() {
                task = makingTask(taskId);
            }

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void update_invalid_update_task_by_id() {
                //given
                String errorMessage = "Task not found: " + taskId;

                //when then
                assertThatThrownBy(() -> {
                    taskService.updateTask(taskId, task);
                }).isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(errorMessage);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask(id) 메소드는")
    class Describe_delete_task {
        @Nested
        @DisplayName("목록에 있는 할 일의 삭제를 요청하면")
        class Context_vialid_delete_task {
            private Long taskId = 1L;
            private Task returnTask;

            @BeforeEach
            void setUpValidDelete() {
                //given
                task = makingTask(1L);
                taskService.createTask(task);
                //when
                returnTask = taskService.deleteTask(taskId);
            }

            @Test
            @DisplayName("목록의 할 일을 삭제한다.")
            void delete_valid_delete_task() {
                //then
                assertThat(returnTask).isNotNull();
            }

            @Test
            @DisplayName("목록의 Task를 삭제 후 삭제한 Task의 정보를 리턴한다.")
            void delete_valid_delete_task_info() {
                //then
                assertThat(returnTask)
                        .extracting("id")
                        .isEqualTo(task.getId());
                assertThat(returnTask)
                        .extracting("title")
                        .isEqualTo(task.getTitle());
            }
        }

        @Nested
        @DisplayName("목록에 없는 Task의 삭제를 요청하면")
        class Context_invalid_delete_task {
            private Long taskId = 2L;

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void delete_invalid_delete_task_exception() {
                //given
                String errorMessage = "Task not found: " + taskId;

                //when then
                assertThatThrownBy(() -> {
                    taskService.deleteTask(taskId);
                }).isInstanceOf(TaskNotFoundException.class)
                        .hasMessage(errorMessage);
            }
        }
    }
}
