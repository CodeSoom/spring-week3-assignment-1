package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.NewTask;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        List<Task> subject() {
            return taskService.getTasks();
        }

        @Nested
        @DisplayName("DB에 데이터가 존재하지 않는다면")
        class Context_with_empty_db {
            @Test
            @DisplayName("빈 ArrayList를 리턴한다")
            void it_returns_empty_arraylist() {
                List<Task> findTasks = subject();

                assertThat(findTasks).hasSize(0);
                assertThat(findTasks).isEqualTo(new ArrayList<>());
            }
        }

        @Nested
        @DisplayName("DB에 데이터가 존재한다면")
        class Context_with_non_empty_db extends NewTask {
            private Task task1, task2;
            private int expectedSize;

            @BeforeEach
            void prepareTests() {
                task1 = withTitle("할 일 추가1");
                task2 = withTitle("할 일 추가2");

                taskService.createTask(task1);
                taskService.createTask(task2);

                expectedSize = taskService.getTasks().size();
            }

            @Test
            @DisplayName("존재하는 모든 할 일을 리턴한다")
            void it_returns_all_tasks() {
                List<Task> findTasks = subject();
                final int actualSize = findTasks.size();

                assertThat(actualSize).isEqualTo(expectedSize);
                assertThat(findTasks.get(0).getTitle()).isEqualTo(task1.getTitle());
                assertThat(findTasks.get(1).getTitle()).isEqualTo(task2.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        @Nested
        @DisplayName("새로운 할 일이 주어지면")
        class Context_with_new_task extends NewTask {
            private final Long TASK_ID = 1L;
            private final String TITLE = "할 일 추가";
            private final Task givenTask = withTitle(TITLE);
            private int expectedSize;

            @BeforeEach
            void prepareTest() {
                expectedSize = taskService.getTasks().size() + 1;
            }

            @Test
            @DisplayName("DB에 등록하고 등록된 할 일을 리턴한다")
            void it_returns_registered_task() {
                Task actualTask = taskService.createTask(givenTask);

                final int actualSize = taskService.getTasks().size();
                final Task actualFindTask = taskService.getTask(TASK_ID);

                assertThat(actualSize).isEqualTo(expectedSize);
                assertThat(actualTask.getTitle()).isEqualTo(givenTask.getTitle());
                assertThat(actualFindTask.getTitle()).isEqualTo(givenTask.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("검색한 할 일 ID가 존재하면")
        class Context_with_existing_task_id extends NewTask {
            private final Long TASK_ID = 1L;
            private final String TITLE = "할 일 검색";
            private Task givenTask;

            @BeforeEach
            void prepareTests() {
                givenTask = withTitle(TITLE);
                taskService.createTask(givenTask);
            }

            @Test
            @DisplayName("해당 ID의 할 일을 리턴한다")
            void it_returns_found_task() {
                Task actualTask = taskService.getTask(TASK_ID);

                assertThat(actualTask.getTitle()).isEqualTo(givenTask.getTitle());
            }

        }

        @Nested
        @DisplayName("검색한 할 일 ID가 존재하지 않는다면")
        class Context_with_non_existing_task_id {
            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
            }
        }

    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("수정요청한 할 일 ID가 존재한다면")
        class Context_with_existing_task_id extends NewTask {
            private  final Long TASK_ID = 1L;
            private final String TITLE = "할 일 수정";
            private final Task givenTask = withTitle(TITLE);


            @BeforeEach
            void prepareTests() {
                Task task = withTitle("할 일 추가");

                taskService.createTask(task);
            }

            @Test
            @DisplayName("해당 할 일을 수정하고 수정된 할 일을 리턴한다")
            void it_returns_modified_task() {
                Task updatedTask = taskService.updateTask(TASK_ID, givenTask);

                assertThat(updatedTask.getTitle()).isEqualTo(givenTask.getTitle());
            }
        }

        @Nested
        @DisplayName("수정요청한 할 일 ID가 존재하지 않는다면")
        class Context_with_non_existing_task_id extends NewTask {
            private final Task givenTask = withTitle("할 일 수정예외");

            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.updateTask(100L, givenTask)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        final Long TASK_ID = 1L;

        @Nested
        @DisplayName("삭제요청한 할 일 ID가 존재한다면")
        class Context_with_existing_task_id extends NewTask {
            private int beforeTotalCount;
            private Task expectedTask;

            @BeforeEach
            void prepareTests() {
                Task task = withTitle("할 일 추가");
                expectedTask = taskService.createTask(task);
                beforeTotalCount = taskService.getTasks().size();
            }

            @Test
            @DisplayName("해당 할 일을 삭제하고 삭제된 할 일을 리턴한다")
            void it_returns_modified_task() {
                final Task deletedTask = taskService.deleteTask(TASK_ID);
                final int afterTotalCount = taskService.getTasks().size();

                assertThat(deletedTask.getTitle()).isEqualTo(expectedTask.getTitle());
                assertThat(afterTotalCount).isEqualTo(beforeTotalCount - 1);
                assertThatThrownBy(() -> taskService.getTask(deletedTask.getId())).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("삭제요청한 할 일 ID가 존재하지 않는다면")
        class Context_with_non_existing_task_id {
            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(100L)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}