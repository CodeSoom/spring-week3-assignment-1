package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("Task 클래스")
class TaskTest {
    private Task task;

    @BeforeEach
    void setUpTask() {
        task = new Task();
    }

    @Nested
    @DisplayName("getId 메소드는")
    class Describe_getId {

        @Nested
        @DisplayName("만약 조회하는 id가 존재한다면")
        class Context_with_exist_id {
            @BeforeEach
            void setUpId() {
                task.setId(1L);
            }

            @Test
            @DisplayName("id를 리턴합니다.")
            void it_returns_exist_id() {
                assertThat(task.getId()).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("만약 조회하는 id가 존재하지 않는다면")
        class Context_with_not_exist_id {

            @Test
            @DisplayName("null를 리턴한다.")
            void it_returns_null() {
                assertThat(task.getId()).isNull();
            }
        }

    }

    @Nested
    @DisplayName("setId 메소드는")
    class Describe_setId {

        @Test
        @DisplayName("입력받은 id로 설정한다.")
        void it_returns_exist_id() {
            task.setId(1L);
            assertThat(task.getId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("getTitle 메소드는")
    class Describe_getTitle {

        @Nested
        @DisplayName("만약 조회하는 title이 존재한다면")
        class Context_with_exist_title {
            @BeforeEach
            void setUpTitle() {
                task.setTitle("default title");
            }

            @Test
            @DisplayName("title를 리턴한다.")
            void it_returns_exist_title() {
                assertThat(task.getTitle()).isEqualTo("default title");
            }
        }

        @Nested
        @DisplayName("만약 조회하는 title이 존재하지 않는다면")
        class Context_with_not_exist_title {

            @Test
            @DisplayName("null를 리턴한다.")
            void it_returns_null() {
                assertThat(task.getTitle()).isNull();
            }
        }
    }

    @Nested
    @DisplayName("setTitle 메소드는")
    class Describe_setTitle {

        @Test
        @DisplayName("입력받은 title로 설정합니다.")
        void it_returns_exist_title() {
            task.setTitle("default title");
            assertThat(task.getTitle()).isEqualTo("default title");
        }
    }
}
