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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("TaskService")
class TaskServiceTest {
    private TaskService taskService;
    private List<Task> tasks;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
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
        @DisplayName("Task 등록 요청을 보내면")
        class Context_valid_create_task {
            @Test
            @DisplayName("Task를 목록에 등록한다.")
            void new_create_task() {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                given(taskService.createTask(task))
                        .willReturn(task);

                //when
                Task newTask = taskService.createTask(task);

                //then
                assertThat(newTask)
                        .isNotNull();
            }

            @Test
            @DisplayName("Task를 등록한 뒤에 동일한 Data의 Task를 반환한다.")
            void create_task_return_task() {
                //given
                tasks = mock(List.class);
                Long taskId = 1L;
                Task task = makingTask(1L);
                given(taskService.createTask(task))
                        .willReturn(task);

                //when
                Task returnTask = taskService.createTask(task);

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
        class Context_empty_getTask {
            @Test
            @DisplayName("비어 있는 목록을 반환 한다.")
            public void get_empty_tasks() {
                //given
                tasks = new ArrayList<>();
                given(taskService.getTasks())
                        .willReturn(tasks);

                //when
                List<Task> returnTasks = taskService.getTasks();

                //then
                assertThat(returnTasks)
                        .isEmpty();
            }
        }

        @Nested
        @DisplayName("Task 목록이 존재하면")
        class Context_not_empty_tasks {
            @Test
            @DisplayName("비어 있지 않은 목록을 반환 한다.")
            public void get_not_empty_tasks() {
                //given
                tasks = new ArrayList<>();
                tasks.add(makingTask(1L));
                tasks.add(makingTask(2L));
                given(taskService.getTasks()).willReturn(tasks);

                //when
                List<Task> returnTasks = taskService.getTasks();

                //then
                assertThat(returnTasks).isNotEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask(id) 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("존재 하는 Task를 호출 한다면")
        class Context_valid_get_task {
            @Test
            @DisplayName("목록에 있는 Task를 반환 한다.")
            void get_valid_task() {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                given(taskService.getTask(taskId))
                        .willReturn(task);

                //when
                Task newTask = taskService.getTask(taskId);

                //then
                assertThat(newTask)
                        .extracting("id").isEqualTo(taskId);
            }
        }

        @Nested
        @DisplayName("목록에 없는 Task를 호출 한다면")
        class Context_invalid_get_task {
            @Test
            @DisplayName("TaskNotFoundException을 발생한다.")
            void get_invalid_get_task_exception() {
                //given
                tasks = mock(List.class);
                Long taskId = 2L;
                String errorMessage = "Task not found: " + taskId;
                Task task = makingTask(taskId);
                given(taskService.getTask(taskId))
                        .willThrow(new TaskNotFoundException(taskId));

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
        @DisplayName("목록에 있는 Task의 수정을 요청하면")
        class Context_valid_update_task {

            @Test
            @DisplayName("목록의 Task를 수정한다.")
            void update_valid_update_task_by_id() {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                given(taskService.updateTask(taskId, task))
                        .willReturn(task);

                //when
                Task returnTask = taskService.updateTask(taskId, task);

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
            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void update_invalid_update_task_by_id() {
                //given
                Long taskId = 1L;
                String errorMessage = "Task not found: " + taskId;
                Task task = makingTask(taskId);
                given(taskService.updateTask(taskId, task))
                        .willThrow(new TaskNotFoundException(taskId));

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
        @DisplayName("목록에 있는 Task의 삭제를 요청하면")
        class Context_vialid_delete_task {
            @Test
            @DisplayName("목록의 Task를 삭제한다.")
            void delete_valid_delete_task() {
                //given
                Long taskId = 1L;
                Task task = makingTask(1L);
                given(taskService.deleteTask(taskId)).willReturn(task);

                //when
                Task returnTask = taskService.deleteTask(taskId);

                //then
                assertThat(returnTask).isNotNull();
            }
            @Test
            @DisplayName("목록의 Task를 삭제 후 삭제한 Task의 정보를 리턴한다.")
            void delete_valid_delete_task_info(){
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                given(taskService.deleteTask(taskId)).willReturn(task);

                //when
                Task returnTask = taskService.deleteTask(taskId);

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
        class Context_invalid_delete_task{
            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void delete_invalid_delete_task_exception(){
                //given
                Long taskId = 2L;
                String errorMessage = "Task not found: " + taskId;
                given(taskService.deleteTask(taskId))
                        .willThrow(new TaskNotFoundException(taskId));

                //when then
                assertThatThrownBy(() -> {
                    taskService.deleteTask(taskId);
                }).isInstanceOf(TaskNotFoundException.class)
                .hasMessage(errorMessage);
            }
        }
    }
}
