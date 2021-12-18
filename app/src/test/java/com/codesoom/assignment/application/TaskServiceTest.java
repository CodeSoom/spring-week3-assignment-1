package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private static final String TASK_TITLE = "test";
    private static final Long EXIST_ID = 1L;
    private static final Long NOT_EXIST_ID = 0L;

    private TaskService taskService;

    void prepareService() {
        taskService = new TaskService();
    }

    void prepareTask() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @DisplayName("getTasks 메서드는")
    @Nested
    class Describe_getTasks {

        List<Task> subject() {
            return taskService.getTasks();
        }

        @DisplayName("등록된 Task가 있다면")
        @Nested
        class Context_with_task {
            @BeforeEach
            void prepare() {
                prepareService();
                prepareTask();
            }

            @DisplayName("등록된 Task의 리스트를 반환한다.")
            @Test
            void it_returns_list_of_tasks() {
                List<Task> result = subject();
                assertThat(result).hasSize(1);
                assertThat(result.get(0).getTitle()).isEqualTo(TASK_TITLE);
                assertThat(result.get(0).getId()).isEqualTo(1L);
            }
        }

        @DisplayName("등록된 Task가 없다면")
        @Nested
        class Context_without_task {
            @BeforeEach
            void prepare() {
                prepareService();
            }

            @DisplayName("비어있는 리스트를 반환한다.")
            @Test
            void It_returns_empty_list() {
                List<Task> result = subject();
                assertThat(result).isEmpty();
            }
        }
    }

    @DisplayName("getTask 메서드는")
    @Nested
    class Describe_getTask {
        @DisplayName("등록된 Task의 id가 주어진다면")
        @Nested
        class Context_with_exist_id {
            Task subject() {
                return taskService.getTask(EXIST_ID);
            }

            @BeforeEach
            void prepare() {
                prepareService();
                prepareTask();
            }

            @DisplayName("해당 id의 Task를 반환한다.")
            @Test
            void it_returns_task() {
                Task result = subject();
                assertThat(result.getTitle()).isEqualTo(TASK_TITLE);
                assertThat(result.getId()).isEqualTo(EXIST_ID);
            }
        }

        @DisplayName("등록되지 않은 Task의 id가 주어진다면")
        @Nested
        class Context_with_not_exist_id {
            Task subject() {
                return taskService.getTask(NOT_EXIST_ID);
            }

            @BeforeEach
            void prepare() {
                prepareService();
            }

            @DisplayName("'Task를 찾을수 없다'는 예외를 던진다.")
            @Test
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> subject()).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @DisplayName("createTask 메서드는")
    @Nested
    class Describe_createTask {
        private static final String NEW_TASK_TITLE = "new";
        Task task;

        void prepareNewTask() {
            task = new Task();
            task.setTitle(NEW_TASK_TITLE);
        }

        @BeforeEach
        void prepare() {
            prepareService();
            prepareNewTask();
        }

        Task subject() {
            return taskService.createTask(task);
        }

        @DisplayName("Task가 주어진다면")
        @Nested
        class Context_with_task {
            @DisplayName("주이진 Task와 동일한 Task를 생성하고, 생성한 Task를 리턴한다.")
            @Test
            void it_returns_created_task() {
                Task result = subject();
                assertThat(result.getTitle()).isEqualTo(NEW_TASK_TITLE);
            }
        }
    }

    @DisplayName("updateTask 메서드는")
    @Nested
    class Describe_updateTask {
        private static final String UPDATE_TASK_TITLE = "update";
        Task task;

        @BeforeEach
        void prepare() {
            prepareService();
            prepareTask();
            prepareUpdateTask();
        }

        void prepareUpdateTask() {
            task = new Task();
            task.setTitle(UPDATE_TASK_TITLE);
        }


        @DisplayName("등록된 task의 id와 Task가 주어진다면")
        @Nested
        class Context_with_exist_id_and_task {
            Task subject() {
                return taskService.updateTask(EXIST_ID, task);
            }

            @DisplayName("주이진 Task와 같도록 등록된 Task를 변경하고 변경된 Task를 리턴한다.")
            @Test
            void it_returns_updated_task() {
                Task result = subject();
                assertThat(result.getTitle()).isEqualTo(UPDATE_TASK_TITLE);
                assertThat(result.getId()).isEqualTo(EXIST_ID);
            }
        }

        @DisplayName("등록되지않은 task의 id가 주어진다면")
        @Nested
        class Context_with_task_not_exist_id {
            Task subject() {
                return taskService.updateTask(NOT_EXIST_ID, task);
            }

            @DisplayName("'Task를 찾을수 없다'는 예외를 던진다.")
            @Test
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> subject()).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @DisplayName("deleteTask 메서드는")
    @Nested
    class Describe_deleteTask {
        @BeforeEach
        void prepare() {
            prepareService();
            prepareTask();
        }

        @DisplayName("등록된 task의 id가 주어진다면")
        @Nested
        class Context_with_exist_id {
            Task subject() {
                return taskService.deleteTask(EXIST_ID);
            }

            @DisplayName("주어진 id의 Task를 삭제하고 삭제한 Task를 리턴한다.")
            @Test
            void it_returns_deleted_task() {
                Task result = subject();
                assertThat(result.getTitle()).isEqualTo(TASK_TITLE);
                assertThat(result.getId()).isEqualTo(EXIST_ID);
            }
        }

        @DisplayName("등록되지않은 Task의 id가 주어진다면")
        @Nested
        class Context_with_task_not_exist_id {
            Task subject() {
                return taskService.deleteTask(NOT_EXIST_ID);
            }

            @DisplayName("'Task를 찾을수 없다'는 예외를 던진다.")
            @Test
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> subject()).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
