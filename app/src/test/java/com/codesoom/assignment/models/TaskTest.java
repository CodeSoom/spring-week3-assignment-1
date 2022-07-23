package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스")
class TaskTest {
    private static final String FIXTURE_TITLE = "title";
    private Task task;

    @BeforeEach
    void beforeEach() {
        task = new Task(null);
    }

    @Nested
    @DisplayName("getId 메소드는")
    class Describe_getId {
        @Nested
        @DisplayName("id를 세팅안했을 때")
        class Context_didNotSetId {
            @Test
            @DisplayName("null을 반환한다")
            void it_returnsNull() {
                final Long result = task.getId();

                assertThat(result).isNull();
            }
        }

        @Nested
        @DisplayName("id를 세팅했을 때")
        class Context_didSetId {
            @BeforeEach
            void context() {
                task.setId(1L);
            }

            @Test
            @DisplayName("세팅된 id를 반환한다")
            void it_returnsId() {
                final Long actual = task.getId();

                assertThat(actual).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("id를 변경했을 때")
        class Context_didChangeId {
            @BeforeEach
            void context() {
                task.setId(1L);
                task.setId(2L);
            }

            @Test
            @DisplayName("마지막으로 세팅된 id를 반환한다")
            void it_returnsId() {
                final Long result = task.getId();

                assertThat(result).isEqualTo(2L);
            }
        }
    }

    @Nested
    @DisplayName("getTitle은")
    class Describe_getTitle {
        @Nested
        @DisplayName("title 세팅 안했을 때")
        class Context_didNotSetTitle {
            @Test
            @DisplayName("null을 반환한다")
            void it_returnsNull() {
                final String result = task.getTitle();

                assertThat(result).isNull();
            }
        }

        @Nested
        @DisplayName("title을 세팅했을 때")
        class Context_didSetTitle {
            @BeforeEach
            void context() {
                task.setTitle(FIXTURE_TITLE);
            }

            @Test
            @DisplayName("세팅된 title을 반환한다")
            void it_returnsTitle() {
                final String result = task.getTitle();

                assertThat(result).isEqualTo(FIXTURE_TITLE);
            }
        }


        @Nested
        @DisplayName("title을 변경했을 때")
        class Context_didChangeTitle {
            @BeforeEach
            void context() {
                task.setTitle(FIXTURE_TITLE + 1);
                task.setTitle(FIXTURE_TITLE + 2);
            }

            @Test
            @DisplayName("마지막으로 세팅한 title을 반환한다")
            void it_returnsTitle() {
                final String result = task.getTitle();

                assertThat(result).isEqualTo(FIXTURE_TITLE + 2);
            }
        }
    }
}
