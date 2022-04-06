package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskController 클래스")
class TaskControllerTest {
    private static final String TASK_TITLE_ONE = "testOne";
    private static final String TASK_TITLE_TWO = "testTwo";
    private static final String UPDATE_TITLE = "otherTest";

    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(TASK_TITLE_ONE);
        controller.create(task);
    }

    @Test
    @DisplayName("list 메소드는 Tasks에 있는 모든 Task를 반환합니다.")
    void list() {
        assertThat(controller.list()).isNotEmpty();
        assertThat(controller.list()).hasSize(1);
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("클라이언트가 요청한 Task의 id가 Tasks에 있으면")
        class Context_with_valid_id {
            @Test
            @DisplayName("id에 해당하는 Task를 반환합니다.")
            void detailWithValidId() {
                assertThat(controller.detail(1L).getTitle()).isEqualTo(TASK_TITLE_ONE);
            }
        }

        @Nested
        @DisplayName("클라이언트가 요청한 Task의 id가 Tasks에 없으면")
        class Context_with_invalid_id {
            @Test
            @DisplayName("TaskNotFoundException 예외를 던집니다.")
            void detailWithInvalidId() {
                assertThatThrownBy(() -> controller.detail(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Test
    @DisplayName("create 메소드는 클라이언트가 요청한 새로운 Task를 Tasks에 추가해줍니다.")
    void create() {
        int oldSize = controller.list().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE_TWO);
        controller.create(task);

        int newSize = controller.list().size();

        assertThat(newSize - oldSize).isEqualTo(1);
        assertThat(controller.list()).hasSize(2);
        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
        assertThat(controller.list().get(0).getTitle()).isEqualTo(TASK_TITLE_ONE);
        assertThat(controller.list().get(1).getId()).isEqualTo(2L);
        assertThat(controller.list().get(1).getTitle()).isEqualTo(TASK_TITLE_TWO);
    }

    @Test
    @DisplayName("update 메소드는 Tasks에서 클라이언트가 요청한 id에 해당하는 Task의 title을 변경해줍니다.")
    void update() {
        Long id = Long.valueOf(controller.list().size());
        Task task = controller.detail(id);

        assertThat(task.getTitle()).isEqualTo(TASK_TITLE_ONE);

        task.setTitle(UPDATE_TITLE);
        controller.update(id, task);
        task = controller.detail(id);

        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @Test
    @DisplayName("patch 메소드는 Tasks에서 클라이언트가 요청한 id에 해당하는 Task의 title을 변경해줍니다.")
    void patch() {
        Long id = Long.valueOf(controller.list().size());
        Task task = controller.detail(id);

        assertThat(task.getTitle()).isEqualTo(TASK_TITLE_ONE);

        task.setTitle(UPDATE_TITLE);
        controller.patch(id, task);
        task = controller.detail(id);

        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @Test
    @DisplayName("delete 메소드는 Tasks에서 클라이언트가 요청한 id에 해당하는 Task를 지웁니다.")
    void delete() {
        int oldSize = controller.list().size();

        controller.delete(Long.valueOf(oldSize));

        int newSize = controller.list().size();

        assertThat(oldSize - newSize).isEqualTo(1);
        assertThat(controller.list()).hasSize(0);
    }
}
