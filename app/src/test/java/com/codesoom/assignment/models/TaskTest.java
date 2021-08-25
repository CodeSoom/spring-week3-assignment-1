package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskTest 클래스")
class TaskTest {
    Task task;

    @BeforeEach
    void setUpTask() {
        task = new Task();
    }

    @Nested
    @DisplayName("getId 메소드는")
    class Describe_getId {
        @Nested
        @DisplayName("저장된 식별 값이 있다면")
        class Context_have_id {

            @BeforeEach
            void prepareId() {
                task.setId(1L);
            }

            @Test
            @DisplayName("식별 값을 리턴합니다")
            void it_returns_id() {
                Long id = task.getId();
                assertThat(id).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("저장된 식별 값이 없다면")
        class Context_no_have_id {
            @Test
            @DisplayName("null을 리턴합니다")
            void it_returns_null() {
                Long id = task.getId();
                assertThat(id).isNull();
            }
        }
    }

    @Nested
    @DisplayName("setId 메소드는")
    class Describe_setId {
        @Test
        @DisplayName("입력받은 식별 값을 저장합니다.")
        void it_saves_id() {
            task.setId(1L);
            assertThat(task.getId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("getTitle 메소드는")
    class Describe_getTitle {
        @Nested
        @DisplayName("저장된 제목이 있다면")
        class Context_have_title {

            @BeforeEach
            void prepareTitle() {
                task.setTitle("title");
            }

            @Test
            @DisplayName("제목을 리턴 합니다")
            void it_returns_title() {
                assertThat(task.getTitle()).isEqualTo("title");
            }
        }

        @Nested
        @DisplayName("저장된 제목이 없다면")
        class Context_no_have_title {
            @Test
            @DisplayName("null을 리턴 합니다")
            void it_returns_title() {
                assertThat(task.getTitle()).isNull();
            }
        }
    }

    @Nested
    @DisplayName("setTitle 메소드는")
    class Describe_setTitle {
        @Test
        @DisplayName("입력받은 제목을 저장합니다.")
        void it_saves_title() {
            task.setTitle("title");
            assertThat(task.getTitle()).isEqualTo("title");
        }
    }
}
