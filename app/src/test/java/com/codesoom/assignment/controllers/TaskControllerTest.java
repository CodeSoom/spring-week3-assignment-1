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
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskController 클래스")
class TaskControllerTest {
    private static final Long Task_Id = 1L;
    private static final String Task_Title_One = "test_One";
    private static final String Task_Title_Two = "test_Two";

    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(Task_Title_One);
        taskController.create(task);
    }

    @Test
    @DisplayName("만약 Task 가 하나만 존재한다면, list 메서드로 조회시 Task 의 갯수는 1개이다.")
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

                assertThat(detail_Title).isEqualTo(Task_Title_One);
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
    @DisplayName("만약 Task 가 하나만 존재한다면, create 메서드로 생성시 Task 의 id 값이 1 증가한 상태로 Tasks 에 추가된다.")
    void create() {
        final Long new_size = Task_Id + 1;
        final Task task = new Task();
        task.setTitle(Task_Title_Two);

        taskController.create(task);
        final int actual_size = taskController.list().size();
        assertThat(new_size).isEqualTo(actual_size);
    }

}
