package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.exception.TaskHasInvalidTitleException;
import com.codesoom.assignment.exception.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.persistence.NamedEntityGraph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService test")
class TaskServiceTest {

    private TaskService taskService;
    private final Long ID = 1L;
    private final Long NOT_EXIST_ID = 100L;
    private final String TITLE_TEST_VALUE = "TEST";
    private final String MODIFY_POSTFIX = "_MODIFY";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    @DisplayName("list 메소드")
    class list {

        @Nested
        @DisplayName("할일이 존재하지 않으면")
        class when_list_is_empty {

            @Test
            @DisplayName("빈 목록을 반환합니다.")
            void list() {
                assertThat(taskService.list()).isEmpty();
            }
        }

        @Nested
        @DisplayName("할일이 존재하면")
        class when_list_is_not_empty {

            @Test
            @DisplayName("할일 목록을 반환합니다.")
            void list() {
                Task task = new Task(ID, TITLE_TEST_VALUE);
                taskService.create(task);

                assertThat(taskService.list().size()).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드")
    class detail {

        @Nested
        @DisplayName("id를 찾을 수 없다면")
        class when_not_found_task_with_id {

            @Test
            @DisplayName("TaskNotFoundException 예외를 발생시킵니다.")
            void detail() {
                assertThatThrownBy(() -> taskService.detail(NOT_EXIST_ID)).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("id를 찾았다면")
        class when_found_task {

            @Test
            @DisplayName("할일 목록을 반환합니다.")
            void detail() {
                Task task = new Task(ID, TITLE_TEST_VALUE);
                taskService.create(task);
                assertThat(taskService.detail(ID).equals(task)).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("create 메소드")
    class create {

        @Nested
        @DisplayName("title이 유효하지 않으면")
        class when_has_invalid_title {
            @Test
            @DisplayName("TaskHasInvalidTaskException 예외가 발생합니다.")
            void create() {
                Task task = new Task(ID, null);
                assertThatThrownBy(() -> taskService.create(task)).isInstanceOf(TaskHasInvalidTitleException.class);
            }
        }

        @Nested
        @DisplayName("id와 title이 유효하면")
        class when_has_valid_input {
            @Test
            @DisplayName("title이 동일한 task가 생성됩니다.")
            void create() {
                Task task = new Task(ID, TITLE_TEST_VALUE);

                assertThat(taskService.create(task).currentTitle()).isEqualTo(task.currentTitle());
                assertThat(taskService.list().size()).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("update 메소드")
    class update {

        private Task change;

        @BeforeEach
        void setUp() {
            Task task = new Task(ID, TITLE_TEST_VALUE);
            taskService.create(task);

            change = new Task(null, TITLE_TEST_VALUE + MODIFY_POSTFIX);
        }

        @Nested
        @DisplayName("존재하지 않는 id라면")
        class when_has_invalid_id {

            @Test
            @DisplayName("TaskNotFoundException 예외를 발생시킵니다.")
            void update() {

                assertThatThrownBy(() -> taskService.update(NOT_EXIST_ID, change)).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 title이라면")
        class when_has_invalid_title {

            @Test
            @DisplayName("TaskHasInvalidTitleException 예외를 발생시킵니다.")
            void update() {
                Task task = new Task(null, null);
                assertThatThrownBy(() -> taskService.update(ID, task)).isInstanceOf(TaskHasInvalidTitleException.class);
            }
        }

        @Nested
        @DisplayName("id와 title이 유효하면")
        class when_has_valid_input {

            @Test
            @DisplayName("변경된 할일 목록을 반환합니다.")
            void update() {
                assertThat(taskService.update(ID, change).currentTitle()).isEqualTo(change.currentTitle());
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드")
    class delete {

        @BeforeEach
        void setUp() {
            Task task = new Task(ID, TITLE_TEST_VALUE);
            taskService.create(task);
        }

        @Nested
        @DisplayName("존재하지 않는 id라면")
        class when_has_invalid_id {

            @Test
            @DisplayName("TaskNotFoundException 예외를 발생 시킵니다.")
            void delete() {
                assertThatThrownBy(() -> taskService.delete(NOT_EXIST_ID)).isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("id가 유효하다면")
        class when_has_valid_id {

            @Test
            @DisplayName("할일 목록의 크기가 1 감소합니다.")
            void delete_decrease_size() {
                taskService.delete(ID);
                assertThat(taskService.list().size()).isEqualTo(0);
                assertThatThrownBy(() -> taskService.delete(ID)).isInstanceOf(TaskNotFoundException.class);
            }

            @Test
            @DisplayName("재조회 시, TaskNotFoundException 예외가 발생합니다.")
            void delete_TaskNotFoundException() {
                taskService.delete(ID);
                assertThat(taskService.list().size()).isEqualTo(0);
                assertThatThrownBy(() -> taskService.delete(ID)).isInstanceOf(TaskNotFoundException.class);
            }
        }

    }

}