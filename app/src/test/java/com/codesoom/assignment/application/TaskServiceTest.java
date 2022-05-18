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
            @DisplayName("할일 목록을 반환합니다.")
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
        @DisplayName("id와 task를 입력했을 때")
        class when_create_new_task_with_id_and_task {

            @Nested
            @DisplayName("id가 유효하지 않으면")
            class has_invalid_id {
                @Test
                @DisplayName("TaskHasInvalidIdException 예외가 발생합니다.")
                void create() {
                    
                }
            }

            @Nested
            @DisplayName("title이 유효하지 않으면")
            class has_invalid_title {
                @Test
                @DisplayName("TaskHasInvalidTaskException 예외가 발생합니다.")
                void create() {

                }
            }

            @Nested
            @DisplayName("id와 title이 유효하면")
            class has_valid_input {
                @Test
                @DisplayName("title이 동일한 task가 생성됩니다.")
                void create() {

                }
            }
        }
    }

}