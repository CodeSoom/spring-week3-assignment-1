package com.codesoom.assignment.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task test")
class TaskTest {

    private Task task;
    private Long ID = 0L;
    private final String TITLE_TEST_VALUE = "TEST";
    private final String MODIFY_POSTFIX = "_MODIFY";

    @Nested
    @DisplayName("hasValidTitle 메소드")
    class validate_Title {

        @Nested
        @DisplayName("타이틀이 유효하면")
        class when_title_is_valid {

            @Test
            @DisplayName("true를 반환합니다.")
            void valiateTitle() {
                task = new Task(ID, TITLE_TEST_VALUE);

                assertThat(task.hasValidTitle()).isTrue();
            }
        }

        @Nested
        @DisplayName("타이틀이 유효하지 않으면")
        class when_title_is_invalid {

            @Test
            @DisplayName("false를 반환합니다.")
            void valiateTitle() {
                task = new Task(null, null);

                assertThat(task.hasValidTitle()).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("changeTitle 메소드")
    class change_title_current_title {

        @BeforeEach
        void setUp() {
            task = new Task(ID, TITLE_TEST_VALUE);
        }

        @Nested
        @DisplayName("타이틀이 변경되지 않으면")
        class when_before_change_title {

            @Test
            @DisplayName("현 시점의 타이틀을 갖고 있어야 합니다.")
            void currentTitle() {
                assertThat(task.currentTitle()).isEqualTo(TITLE_TEST_VALUE);
            }
        }

        @Nested
        @DisplayName("타이틀을 변경하면")
        class when_change_title {

            @Test
            @DisplayName("반환된 객체는 변경된 타이틀을 갖고 있어야 합니다.")
            void changeTitle() {
                Task change = new Task(null, TITLE_TEST_VALUE + MODIFY_POSTFIX);
                String currentTitle = task.changeTitle(change).currentTitle();

                assertThat(currentTitle).isEqualTo(TITLE_TEST_VALUE + MODIFY_POSTFIX);
            }
        }
    }

    @Nested
    @DisplayName("createNewTask 메소드")
    class create_new_task {

        @BeforeEach
        void setUp() {
            task = new Task(ID, TITLE_TEST_VALUE);
        }

        @Nested
        @DisplayName("id와 task를 입력하면")
        class when_create_new_task_with_other_task {

            @Test
            @DisplayName("title 값이 같은 객체가 생성됩니다.")
            void createNewTask() {
                Task newTask = Task.createNewTask(100L, task);

                assertThat(newTask.currentTitle()).isEqualTo(task.currentTitle());
            }
        }
    }

}