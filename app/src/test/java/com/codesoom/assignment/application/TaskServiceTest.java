package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.NewTask;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
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
        @DisplayName("DB에 데이터가 없는데 조회를 한다면")
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
        @DisplayName("DB에 데이터가 있는데 조회를 한다면")
        class Context_with_non_empty_db extends NewTask {
            @Test
            @DisplayName("등록된 모든 할 일을 리턴한다")
            void it_returns_all_tasks() {
                // given
                Task task1 = withTitle("할 일 추가1");
                Task task2 = withTitle("할 일 추가2");


                taskService.createTask(task1);
                taskService.createTask(task2);

                // when
                List<Task> findTasks = subject();

                final int expectSize = 2;
                final int actualSize = findTasks.size();

                // then
                assertThat(actualSize).isEqualTo(expectSize);
                assertThat(findTasks.get(0).getTitle()).isEqualTo(task1.getTitle());
                assertThat(findTasks.get(1).getTitle()).isEqualTo(task2.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        final Long TASK_ID = 1L;
        final String TITLE = "할 일 추가";
        @Nested
        @DisplayName("새로운 할 일을 추가한다면")
        class Context_with_new_task extends NewTask {
            @Test
            @DisplayName("DB에 등록 후 등록된 할 일을 리턴한다")
            void it_returns_registered_task() {
                // given
                Task task = withTitle(TITLE);

                // when
                Task actualTask = taskService.createTask(task);

                // then
                assertThat(taskService.getTasks()).hasSize(1);
                assertThat(taskService.getTask(TASK_ID).getTitle()).isEqualTo(TITLE);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("존재하는 할 일 ID로 검색한다면")
        class Context_with_existing_task_id extends NewTask {
            final Long TASK_ID = 1L;
            final String TITLE = "할 일 검색";

            @Test
            @DisplayName("검색된 할 일을 리턴한다")
            void it_returns_found_task() {
                // given
                Task task = withTitle(TITLE);

                taskService.createTask(task);
                // when
                Task findTask = taskService.getTask(TASK_ID);

                // then
                assertThat(findTask.getTitle()).isEqualTo(TITLE);
            }

        }

        @Nested
        @DisplayName("존재하지않는 할 일 ID로 검색한다면")
        class Context_with_non_existing_task_id {
            @Test
            @DisplayName("TaskNotFoundException 예외가 발생한다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.getTask(100L)).isInstanceOf(TaskNotFoundException.class);
            }
        }

    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        @Nested
        @DisplayName("존재하는 할 일 ID로 수정요청한다면")
        class Context_with_existing_task_id extends NewTask {
            final Long TASK_ID = 1L;
            final String TITLE = "할 일 수정";

            @BeforeEach
            void insertTask() {
                Task task = withTitle("할 일 추가");

                taskService.createTask(task);
            }

            @Test
            @DisplayName("DB를 수정 후 수정된 할 일을 리턴한다")
            void it_returns_modified_task() {
                // given
                Task newTask = withTitle(TITLE);

                // when
                Task updatedTask = taskService.updateTask(TASK_ID, newTask);

                // then
                assertThat(updatedTask.getTitle()).isEqualTo(TITLE);
            }
        }

        @Nested
        @DisplayName("존재하지않는 할 일 ID로 수정요청한다면")
        class Context_with_non_existing_task_id extends NewTask {
            @Test
            @DisplayName("TaskNotFoundException 예외가 발생한다")
            void it_throws_task_not_found_exception() {
                Task newTask = withTitle("할 일 수정예외");

                assertThatThrownBy(() -> taskService.updateTask(100L, newTask)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        final Long TASK_ID = 1L;

        @Nested
        @DisplayName("존재하는 할 일 ID로 삭제요청한다면")
        class Context_with_existing_task_id extends NewTask {
            @BeforeEach
            void insertTask() {
                Task task = withTitle("할 일 추가");
                taskService.createTask(task);
            }

            @Test
            @DisplayName("DB를 삭제 후 삭제된 할 일을 리턴한다")
            void it_returns_modified_task() {
                final int beforeTotalCount = taskService.getTasks().size();
                final Task expectedTask = taskService.getTask(TASK_ID);

                // when
                final Task deletedTask = taskService.deleteTask(TASK_ID);
                final int afterTotalCount = taskService.getTasks().size();

                // then
                assertThat(deletedTask.getTitle()).isEqualTo(expectedTask.getTitle());
                assertThat(afterTotalCount).isEqualTo(beforeTotalCount - 1);
                assertThat(deletedTask).isEqualTo(expectedTask);
            }
        }

        @Nested
        @DisplayName("존재하지않는 할 일 ID로 삭제요청한다면")
        class Context_with_non_existing_task_id {
            @Test
            @DisplayName("TaskNotFoundException 예외가 발생한다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.deleteTask(100L)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}