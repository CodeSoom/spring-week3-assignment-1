package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스")
class TaskTest {
    private static final String FIXTURE_TITLE = "title";
    private static final long FIXTURE_ID = 0L;
    private Task task;

    @BeforeEach
    void beforeEach() {
        task = new Task();
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
    
    @DisplayName("title 지정 안했을 때 getTitle 호출하면 null 반환")
    @Test
    void givenWithoutSetTitle_whenGetTitle_thenNull() {
        // given
        final Task task = new Task();

        // when
        final String actual = task.getTitle();

        // then
        assertThat(actual).isNull();
    }

    @DisplayName("title 지정 하고 때 getTitle 호출하면 지정된 title 반환")
    @Test
    void givenSetTitle_whenGetTitle_thenReturnTitle() {
        // given
        final Task task = new Task();
        task.setTitle(FIXTURE_TITLE);

        // when
        final String actual = task.getTitle();

        // then
        assertThat(actual).isEqualTo(FIXTURE_TITLE);
    }

    @DisplayName("title 업데이트 하고 때 getTitle 호출하면 업데이트한 title 반환")
    @Test
    void givenUpdateTitle_whenGetTitle_thenReturnTitle() {
        // given
        final Task task = new Task();
        task.setTitle(FIXTURE_TITLE);
        task.setTitle(FIXTURE_TITLE + "new");

        // when
        final String actual = task.getTitle();

        // then
        assertThat(actual).isEqualTo(FIXTURE_TITLE + "new");
    }

    @DisplayName("id, title 지정하고 getId, getTitle 호출하면 id와 title 반환")
    @Test
    void givenSetIdAndSetTitle_whenGetIdAndGetTitle_thenReturnIdAndTitle() {
        // given
        final Task task = new Task();
        task.setId(FIXTURE_ID);
        task.setTitle(FIXTURE_TITLE);

        // when
        final String title = task.getTitle();
        final Long id = task.getId();

        // then
        assertThat(id).isEqualTo(FIXTURE_ID);
        assertThat(title).isEqualTo(FIXTURE_TITLE);
    }
}
