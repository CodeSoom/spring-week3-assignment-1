package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskService test")
class TaskServiceTest {

    private TaskService taskService;
    private Long ID = 0L;
    private final String TITLE_TEST_VALUE = "TEST";
    private final String MODIFY_POSTFIX = "_MODIFY";

    @Nested
    @DisplayName("list 메소드")
    class list {

        @BeforeEach
        void setUp() {
            taskService = new TaskService();
        }

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
            @DisplayName("목록을 반환합니다.")
            void list() {
                Task task = new Task(ID, TITLE_TEST_VALUE);
                taskService.create(task);
                assertThat(taskService.list().size()).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("create 메소드")
    class create {

        @Nested
        @DisplayName("할일을 신규 등록했을 때, 정상적으로 등록이 되었다면")
        class when_create_new_task {

            @Test
            @DisplayName("생성된 Task 객체를 반환합니다.")
            void create() {

            }
        }
    }
}