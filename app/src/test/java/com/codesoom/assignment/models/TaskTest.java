package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private Task task;
    private static final String TASK_TITLE = "gigi";
    private static final Long TASK_ID = 1L;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
    }

    @Nested
    @DisplayName("인자가 하나도 없는 생성자는")
    class Describe_no_args_constructor {

        @Test
        @DisplayName("필드값들이 모두 null이다.")
        void it_has_not_any_properties_value() {
            Task task = new Task();

            assertThat(task.getTitle()).isNull();
            assertThat(task.getId()).isNull();
        }
    }

    @Nested
    @DisplayName("getId 메소드는")
    class Describe_getId {

        @Test
        @DisplayName("task의 id값을 반환한다.")
        void it_returns_id_value() {
            Long id = task.getId();

            assertThat(id).isEqualTo(TASK_ID);
        }
    }

    @Nested
    @DisplayName("setId 메소드는")
    class Describe_setId {
        private Long id;

        @BeforeEach
        void setUp() {
            id = 2L;
        }

        @Test
        @DisplayName("id값을 변경한다.")
        void it_changes_id_value() {
            task.setId(id);

            assertThat(task.getId()).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("getTitle 메소드는")
    class Describe_getTitle {

        @Test
        @DisplayName("task의 title값을 반환한다.")
        void it_returns_id_value() {
            String title = task.getTitle();

            assertThat(title).isEqualTo(TASK_TITLE);
        }
    }

    @Nested
    @DisplayName("setTitle 메소드는")
    class Describe_setTitle {
        private String title;

        @BeforeEach
        void setUp() {
            title = "hello";
        }

        @Test
        @DisplayName("title값을 변경한다.")
        void it_changes_title_value() {
            task.setTitle(title);

            assertThat(task.getTitle()).isEqualTo(title);
        }
    }
}
