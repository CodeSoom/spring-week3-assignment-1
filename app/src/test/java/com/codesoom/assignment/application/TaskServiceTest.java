package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)
    private static final String TASK_TITLE_ONE = "testOne";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        final static int ZERO = 0;
        @Nested
        @DisplayName("Task를 추가하기 전에는")
        class Context_beforeAddingTask {
            @Test
            @DisplayName("비어있고, 개수가 0입니다..")
            void getTasksWithEmpty() {
                assertThat(taskService.getTasks()).isEmpty();
                assertThat(taskService.getTasks()).hasSize(ZERO);
            }
        }

        @Nested
        @DisplayName("Task를 5개 추가한 후에는")
        class Context_afterAddingTask {
            final static int TASKS_SIZE = 5;
            final static String TASK_TITLE = "test";

            @BeforeEach
            void setUp() {
                Task task = new Task(TASK_TITLE);
                for (int i = 0; i < TASKS_SIZE; i++) {
                    task.setTitle(TASK_TITLE + (i + 1));
                    taskService.createTask(task);
                }
            }

            @Test
            @DisplayName("비어있지 않고, 개수가 5입니다.")
            void getTasksWithNotEmpty() {
                    assertThat(taskService.getTasks()).isNotEmpty();
                    assertThat(taskService.getTasks()).hasSize(TASKS_SIZE);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("클라이언트가 요청한 Task의 id가 Tasks에 있으면")
        class Context_with_valid_id {
            final long VALID_REQUEST_TASK_ID = 1L;

            @BeforeEach
            void setUp() {
                Task task = new Task(TASK_TITLE_ONE);

                taskService.createTask(task);
            }

            @Test
            @DisplayName("id에 해당하는 Task를 반환합니다.")
            void getTask() {
                assertThat(taskService.getTask(VALID_REQUEST_TASK_ID)
                        .getTitle()).isEqualTo(TASK_TITLE_ONE);
            }
        }

        @Nested
        @DisplayName("클라이언트가 요청한 Task의 id가 Tasks에 없으면")
        class Context_with_invalid_id {
            final long INVALID_REQUEST_TASK_ID = 6L;
            final static int TASKS_MAX_SIZE = 5;
            final static String TASK_DEFAULT_TITLE = "test";

            @BeforeEach
            void setUp() {
                Task task = new Task(TASK_DEFAULT_TITLE);
                for (int i = 0; i < TASKS_MAX_SIZE; i++) {
                    task.setTitle(TASK_DEFAULT_TITLE + (i + 1));
                    taskService.createTask(task);
                }
            }

            @Test
            @DisplayName("TaskNotFoundException 예외를 던집니다.")
            void throwTaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(INVALID_REQUEST_TASK_ID))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        private static final String TASK_TITLE_TWO = "testTwo";
        private final Long NEW_ID = 2L;

        @BeforeEach
        void setUp() {
            Task task = new Task();
            task.setTitle(TASK_TITLE_ONE);
            taskService.createTask(task);
        }

        @Test
        @DisplayName("새로운 Task에 id를 생성해줍니다.")
        void CreateTaskWithGenerateId() {
            Task task = new Task();
            task.setTitle(TASK_TITLE_TWO);

            taskService.createTask(task);

            assertThat(taskService.getTask(taskService.getTasksSize())
                    .getId()).isEqualTo(NEW_ID);
        }

        @Test
        @DisplayName("클라이언트가 요청한 새로운 Task를 Tasks에 추가해줍니다.")
        void CreateTask() {
            Long oldSize = taskService.getTasksSize();

            Task task = new Task();
            task.setTitle(TASK_TITLE_TWO);

            taskService.createTask(task);

            Long newSize = taskService.getTasksSize();

            assertThat(newSize - oldSize).isEqualTo(1);
            assertThat(taskService.getTasks()).hasSize(NEW_ID.intValue());
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        private static final String UPDATE_TITLE = "other";

        @BeforeEach
        void setUp() {
            Task task = new Task();
            task.setTitle(TASK_TITLE_ONE);
            taskService.createTask(task);
        }
        @Test
        @DisplayName("Tasks에서 클라이언트가 요청한 id에 해당하는 Task의 title을 변경해줍니다.")
        void updateTask() {
            Long id = taskService.getTasksSize();
            Task task = taskService.getTask(id);

            assertThat(task.getTitle()).isEqualTo(TASK_TITLE_ONE);

            task.setTitle(UPDATE_TITLE);
            taskService.updateTask(id, task);
            task = taskService.getTask(id);

            assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @BeforeEach
        void setUp() {
            Task task = new Task();
            task.setTitle(TASK_TITLE_ONE);
            taskService.createTask(task);
        }

        @Test
        @DisplayName("Tasks에서 클라이언트가 요청한 id에 해당하는 Task를 지웁니다.")
        void deleteTask() {
            Long oldSize = taskService.getTasksSize();

            taskService.deleteTask(oldSize);

            Long newSize = taskService.getTasksSize();

            assertThat(oldSize - newSize).isEqualTo(1);
            assertThat(taskService.getTasks()).hasSize(0);
        }
    }
}
