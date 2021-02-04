package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스")
class TaskTest {

    private static final String TASK_TITLE = "test";
    private static final String NEW_TASK_TITLE = "new test";

    private static final Long TASK_ID = 10L;
    private static final Long NEW_TASK_ID = 100L;

    private Task task;

    @BeforeEach
    void setUp() {
        // fixture
        task = new Task();
        task.setTitle(TASK_TITLE);
        task.setId(TASK_ID);
    }

    @Nested
    @DisplayName("getId")
    class Describe_getId {
        @Nested
        @DisplayName("id가 존재한다면")
        class Context_task_id_exist {
            @BeforeEach
            void setUp() {
                task = new Task();
                task.setId(TASK_ID);
            }

            @Test
            @DisplayName("id를 리턴한다")
            void it_returns_task_id() {
                assertThat(task.getId()).isEqualTo(TASK_ID);
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않는다면")
        class Context_task_id_does_not_exist {
            @BeforeEach
            void setUp() {
                task = new Task();
            }

            @Test
            @DisplayName("null을 리턴한다")
            void it_returns_null() {
                assertThat(task.getId()).isNull();
            }
        }
    }

    @Nested
    @DisplayName("setId")
    class Describe_setId {
        @Test
        @DisplayName("id를 설정한다")
        void it_set_task_id() {
            task.setId(NEW_TASK_ID);

            assertThat(task.getId()).isEqualTo(NEW_TASK_ID);
        }
    }

    @Nested
    @DisplayName("getTitle")
    class Describe_getTitle {
        @Nested
        @DisplayName("title이 존재한다면")
        class Context_task_title_exist {
            @Test
            @DisplayName("title를 리턴한다")
            void it_returns_task_title() {
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("title이 존재하지 않는다면")
        class Context_task_title_does_not_exist {
            @BeforeEach
            void setUp() {
                task = new Task();
            }

            @Test
            @DisplayName("null을 리턴한다")
            void it_returns_null() {
                assertThat(task.getTitle()).isNull();
            }
        }
    }

    @Nested
    @DisplayName("setTitle")
    class Describe_setTitle {
        @Test
        @DisplayName("title를 설정한다")
        void it_set_task_title() {
            task.setTitle(NEW_TASK_TITLE);

            assertThat(task.getTitle()).isEqualTo(NEW_TASK_TITLE);
        }
    }

}
