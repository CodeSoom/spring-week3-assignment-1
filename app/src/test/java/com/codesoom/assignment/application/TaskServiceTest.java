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
        @DisplayName("데이터가 존재하지 않는다면")
        class Context_with_empty_db {
            @Test
            @DisplayName("빈 데이터를 리턴한다")
            void it_returns_empty_arraylist() {
                List<Task> findTasks = subject();

                assertThat(findTasks).hasSize(0);
                assertThat(findTasks).isEqualTo(new ArrayList<>());
            }
        }

        @Nested
        @DisplayName("데이터가 존재한다면")
        class Context_with_non_empty_db extends NewTask {
            private final List<Task> givenTasks = new ArrayList<>();

            @BeforeEach
            void prepareTests() {
                givenTasks.add(withTitle("할 일 추가1"));
                givenTasks.add(withTitle("할 일 추가2"));

                taskService.createTask(givenTasks.get(0));
                taskService.createTask(givenTasks.get(1));

            }

            @Test
            @DisplayName("모든 할 일을 리턴한다")
            void it_returns_all_tasks() {
                List<Task> findTasks = subject();
                final int actualSize = findTasks.size();

                assertThat(actualSize).isEqualTo(givenTasks.size());
                assertThat(findTasks.get(0).getTitle()).isEqualTo(givenTasks.get(0).getTitle());
                assertThat(findTasks.get(1).getTitle()).isEqualTo(givenTasks.get(1).getTitle());
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

            @Test
            @DisplayName("DB에 등록하고 등록된 할 일을 리턴한다")
            void it_returns_registered_task() {
                final int expectedSize = taskService.getTasks().size() + 1;

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
        @DisplayName("ID가 주어지면")
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
            @DisplayName("할 일을 찾아 리턴한다")
            void it_returns_found_task() {
                Task actualTask = taskService.getTask(TASK_ID);

                assertThat(actualTask.getTitle()).isEqualTo(givenTask.getTitle());
            }

        }

        @Nested
        @DisplayName("유효하지 않은 ID가 주어지면")
        class Context_with_non_existing_task_id {
            @Test
            @DisplayName("예외를 던진다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
            }
        }

    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("ID가 주어지면")
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
            @DisplayName("할 일을 찾아 수정하고 리턴한다")
            void it_returns_modified_task() {
                Task updatedTask = taskService.updateTask(TASK_ID, givenTask);

                assertThat(updatedTask.getTitle()).isEqualTo(givenTask.getTitle());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 ID가 주어지면")
        class Context_with_non_existing_task_id extends NewTask {
            private final Task givenTask = withTitle("할 일 수정예외");

            @Test
            @DisplayName("예외를 던진다")
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
        @DisplayName("ID가 주어지면")
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
            @DisplayName("할 일을 찾아 삭제하고 리턴한다")
            void it_returns_modified_task() {
                final Task deletedTask = taskService.deleteTask(TASK_ID);
                final int afterTotalCount = taskService.getTasks().size();

                assertThat(deletedTask.getTitle()).isEqualTo(expectedTask.getTitle());
                assertThat(afterTotalCount).isEqualTo(beforeTotalCount - 1);
                assertThatThrownBy(() -> taskService.getTask(deletedTask.getId())).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 ID가 주어지면")
        class Context_with_non_existing_task_id {
            @Test
            @DisplayName("예외를 던진다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(100L)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}