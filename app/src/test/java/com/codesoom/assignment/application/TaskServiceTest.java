package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    private TaskService taskService;
    private static final String BEFORE_TASK_TITLE = "before";
    private static final String UPDATE_TASK_TITLE = "updated";
    private static final String CREATE_TASK_TITLE ="created";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task beforeTask = new Task();
        beforeTask.setTitle(BEFORE_TASK_TITLE);
        taskService.createTask(beforeTask);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Test
        @DisplayName("할 일의 목록을 리턴한다")
        void itReturnsListOfTask() {
            List<Task> tasks = taskService.getTasks();
            assertThat(tasks).hasSize(1);

            Task task = taskService.getTasks().get(0);
            assertThat(task.getId()).isEqualTo(1L);
            assertThat(task.getTitle()).isEqualTo(BEFORE_TASK_TITLE);
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("만약 저장되어 있는 할 일의 id가 주어진다면")
        class ContextWithValidId {
            @Test
            @DisplayName("주어진 id에 해당하는 할 일을 리턴한다")
            void itReturnsValidTask() {
                Task task = taskService.getTask(1L);
                assertThat(task.getTitle()).isEqualTo(BEFORE_TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 할 일의 id가 주어진다면")
        class ContextWithInvalidId {
            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void itReturnsErrorMessageException() {
                assertThatThrownBy(() -> taskService.getTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask {
        @Test
        @DisplayName("title을 입력받아 새로운 할 일을 생성하고 할 일을 리턴한다")
        void itReturnsNewTask() {
            int oldIndex = taskService.getTasks().size()-1;
            Long oldId = taskService.getTasks().get(oldIndex).getId();

            Task newTask = new Task();
            newTask.setTitle(CREATE_TASK_TITLE);

            taskService.createTask(newTask);

            assertThat(taskService.getTasks().get(oldIndex+1).getId()).isEqualTo(oldId + 1L);
            assertThat(taskService.getTasks().get(oldIndex+1).getTitle()).isEqualTo(CREATE_TASK_TITLE);
        }
    }

    @Nested
    @DisplayName("updateTask 메서드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("만약 저장되어 있는 할 일의 id가 주어진다면")
        class ContextWithValidId {
            @Test
            @DisplayName("주어진 id에 해당하는 할 일의 title을 수정하고 할 일을 리턴한다")
            void itReturnsValidUpdatedTask() {
                Task source = new Task();
                source.setTitle(UPDATE_TASK_TITLE);
                taskService.updateTask(1L, source);

                Task task = taskService.getTask(1L);
                assertThat(task.getTitle()).isEqualTo(UPDATE_TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 할 일의 id가 주어진다면")
        class ContextWithInvalidId {
            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void itReturnsErrorMessageException() {
                Task source = new Task();
                source.setTitle(UPDATE_TASK_TITLE);

                assertThatThrownBy(() -> taskService.updateTask(100L, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메서드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("만약 저장되어 있는 할 일의 id가 주어진다면")
        class ContextWithValidId {
            @Test
            @DisplayName("유효한 id에 해당하는 할 일을 삭제하고 빈 문자열을 리턴한다")
            void itDeletesTaskAndReturnsEmptyString() {
                taskService.deleteTask(1L);
                assertThat(taskService.getTasks().toString()).isEqualTo("[]");
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 할 일의 id가 주어진다면")
        class ContextWithInvalidId {
            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void itReturnsErrorMessageException() {
                assertThatThrownBy(() -> taskService.deleteTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
