package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskController 클래스")
class TaskControllerTest {
    private static final Long TASK_ID = 1L;
    private static final String TASK_TITLE_ONE = "test_One";
    private static final String TASK_TITLE_TWO = "test_Two";

    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(TASK_TITLE_ONE);
        taskController.create(task);
    }

    @Test
    @DisplayName("만약 Task 가 1개 있다면, list 메서드로 조회시 Task 의 갯수는 1이다.")
    void list() {
        final List<Task> list = taskController.list();

        assertThat(list).hasSize(1);
    }

    @Nested
    @DisplayName("Detail 메서드는")
    class Detail {
        @Nested
        @DisplayName("클라이언트가 요청한 Task 의 id 가 존재하면")
        class valid_id {
            @Test
            @DisplayName("id 에 해당하는 Task 를 반환한다.")
            void detail_valid_id() {
                final String detail_Title = taskController.detail(1L).getTitle();

                assertThat(detail_Title).isEqualTo(TASK_TITLE_ONE);
            }
        }

        @Nested
        @DisplayName("클라이언트가 요청한 Task 의 id 가 존재하지 않으면")
        class invalid_id {
            @Test
            @DisplayName("TaskNotFoundException 을 반환한다.")
            void detail_invalid_id() {
                assertThatThrownBy(() -> taskController.detail(2L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Test
    @DisplayName("만약 Task 가 있다면, create 메서드로 생성시 Task 의 id 값이 1 증가한 상태로 Task 에 추가된다.")
    void create() {
        final int new_size = taskController.list().size() + 1;
        final Task task = new Task();
        task.setTitle(TASK_TITLE_TWO);

        taskController.create(task);
        final int actual_size = taskController.list().size();
        assertThat(new_size).isEqualTo(actual_size);
    }

    @Nested
    @DisplayName("Update 메서드는")
    class update {
        Task task = new Task();
        @BeforeEach
        void update_setUp() {
            task.setTitle("test");

            taskController.create(task);
        }

        @Nested
        @DisplayName("만약 해당 id 가 있으면")
        class valid_id {
            @Test
            @DisplayName("Task 의 title 을 수정하고 해당 title 을 반환한다.")
            void update_valid_id() {
                Task task = new Task();
                task.setTitle(TASK_TITLE_TWO);
                taskController.update(TASK_ID, task);

                Task new_task = taskController.detail(TASK_ID);
                assertThat(new_task.getTitle()).isEqualTo(TASK_TITLE_TWO);
            }
        }

        @Nested
        @DisplayName("만약 해당 id 가 없으면")
        class invalid_id {
            @Test
            @DisplayName("TaskNotFoundException 을 반환한다.")
            void update_invalid_id() {
                final int SIZE_ID = taskController.list().size();
                final Long UNKNOWN_ID = Long.valueOf(SIZE_ID + 10);

                assertThatThrownBy(() -> taskController.update(UNKNOWN_ID, task))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("Delete 메서드는")
    class delete {
        @Test
        @DisplayName("해당 id 가 있으면 Task 의 id 를 삭제한다.")
        void delete() {
            int old_size = taskController.list().size();
            taskController.delete(TASK_ID);
            int new_size = taskController.list().size();

            assertThat(old_size - new_size).isEqualTo(1);
        }
    }
}
