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

        Task task = new Task();
        task.setTitle(TASK_TITLE_ONE);
        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Test
        @DisplayName("모든 Tasks를 반환한다.")
        void getTasks() {
            assertThat(taskService.getTasks()).isNotEmpty();
            assertThat(taskService.getTasks()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("매개변수로 입력 받는 id가 있으면")
        class Context_with_valid_id {
            @Test
            @DisplayName("id에 해당하는 Task를 반환한다.")
            void getTask() {
                assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TASK_TITLE_ONE);
            }
        }

        @Nested
        @DisplayName("매개변수로 입력 받는 id가 없으면")
        class Context_with_invalid_id {
            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다.")
            void throwTaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        private static final String TASK_TITLE_TWO = "testTwo";
        @Test
        @DisplayName("새로운 Task에 id를 생성해줍니다.")
        void CreateTaskWithGenerateId() {
            Task task = new Task();
            task.setTitle(TASK_TITLE_TWO);

            taskService.createTask(task);

            assertThat(taskService.getTask(taskService.getTasksSize())
                    .getId()).isEqualTo(2L);
        }

        @Test
        @DisplayName("새로운 Task를 추가해줍니다.")
        void CreateTask() {
            Long oldSize = taskService.getTasksSize();

            Task task = new Task();
            task.setTitle(TASK_TITLE_TWO);

            taskService.createTask(task);

            Long newSize = taskService.getTasksSize();

            assertThat(newSize - oldSize).isEqualTo(1);
            assertThat(taskService.getTasks()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        private static final String UPDATE_TITLE = "other";
        @Test
        @DisplayName("id에 해당하는 Task의 title을 변경해줍니다.")
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
        @Test
        @DisplayName("id에 해당하는 Task를 지웁니다.")
        void deleteTask() {
            Long oldSize = taskService.getTasksSize();

            taskService.deleteTask(oldSize);

            Long newSize = taskService.getTasksSize();

            assertThat(oldSize - newSize).isEqualTo(1);
            assertThat(taskService.getTasks()).hasSize(0);
        }
    }
}
