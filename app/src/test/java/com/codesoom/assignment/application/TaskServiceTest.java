package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.dto.TaskEditDto;
import com.codesoom.assignment.dto.TaskSaveDto;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private static final String TEST_TASK_TITLE = "TITLE";
    private static final String TEST_TASK_UPDATE_TITLE = "TITLE_UPDATE";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("유효한 할 일 데이터가 주어진다면")
        class Context_valid {

            final TaskSaveDto source = new TaskSaveDto(TEST_TASK_TITLE);

            Task subject() {
                return taskService.createTask(source);
            }

            @Test
            @DisplayName("할 일을 생성하고 생성된 할 일을 리턴한다.")
            void it_save_and_return_savedTask() {
                Task task = subject();
                assertAll(
                        () -> assertThat(task.getId()).isNotNull(),
                        () -> assertThat(task.getTitle()).isEqualTo(TEST_TASK_TITLE)
                );
            }
        }
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("10개의 할 일 목록이 있다면")
        class Context_hasTaskListOf10 {

            final int givenTaskCount = 10;

            @BeforeEach
            void given() {
                IntStream.rangeClosed(1, givenTaskCount)
                        .forEach((index) -> generateTask());
            }

            int subject() {
                List<Task> tasks = taskService.getTasks();
                return tasks.size();
            }

            @Test
            @DisplayName("10개의 할 일 목록을 리턴한다.")
            void it_return_tasks() {
                assertThat(subject()).isEqualTo(givenTaskCount);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {

        @Nested
        @DisplayName("주어진 아이디와 일치하는 할 일이 있다면")
        class Context_existsTask {

            Task givenTask;

            @BeforeEach
            void given() {
                givenTask = generateTask();
            }

            Task subject() {
                return taskService.getTask(givenTask.getId());
            }

            @Test
            @DisplayName("할 일을 리턴 한다.")
            void it_return_task() {
                assertThat(subject()).isEqualTo(givenTask);
            }
        }

        @Nested
        @DisplayName("주어진 아이디와 일치하는 할 일이 없다면")
        class Context_notExistsTask {

            final Long notExistsTaskId = 999L;

            @Test
            @DisplayName("예외를 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskService.getTask(notExistsTaskId)
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {

        final TaskEditDto source = new TaskEditDto(TEST_TASK_UPDATE_TITLE);

        @Nested
        @DisplayName("주어진 아이디와 일치하는 할 일이 있고, 유효한 데이터가 주어진다면")
        class Context_valid {

            Task givenTask;

            @BeforeEach
            void given() {
                givenTask = generateTask();
            }

            Task subject() {
                return taskService.updateTask(givenTask.getId(), source);
            }

            @Test
            @DisplayName("할 일을 변경하고 변경된 할 일을 리턴한다.")
            void it_update_and_updated_task_return() {
                Task updatedTask = subject();
                assertThat(updatedTask.getTitle()).isEqualTo(TEST_TASK_UPDATE_TITLE);
            }
        }

        @Nested
        @DisplayName("주어진 아이디와 일치하는 할 일이 없다면")
        class Context_notExistsTask {

            final Long notExistsTaskId = 999L;

            @Test
            @DisplayName("예외를 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(() -> taskService.updateTask(notExistsTaskId, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("주어진 아이디와 일치하는 할 일이 있다면")
        class Context_existsTask {

            Task givenTask;

            @BeforeEach
            void given() {
                givenTask = generateTask();
            }

            Task subject() {
                return taskService.deleteTask(givenTask.getId());
            }

            @Test
            @DisplayName("할 일을 삭제하고 삭제된 할 일을 리턴한다.")
            void it_delete_and_return_deleted_task() {
                Task deletedTask = subject();
                assertAll(
                        () -> assertThatThrownBy(
                                () -> taskService.getTask(deletedTask.getId())
                        ).isInstanceOf(TaskNotFoundException.class),
                        () -> assertThat(deletedTask).isEqualTo(givenTask)
                );
            }
        }

        @Nested
        @DisplayName("주어진 아이디와 일치하는 할 일이 없다면")
        class Context_notExistsTask {

            final Long notExistsTaskId = 999L;

            @Test
            @DisplayName("예외를 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> taskService.deleteTask(notExistsTaskId)
                ).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    private Task generateTask() {
        TaskSaveDto source = new TaskSaveDto(TEST_TASK_TITLE);
        return taskService.createTask(source);
    }
}
