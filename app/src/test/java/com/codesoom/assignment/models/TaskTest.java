package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스")
class TaskTest {
    private final Long TEST_ID = 1L;
    private final String TEST_TITLE = "Test!";
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @Disabled("뻔한 동작을 테스트합니다")
    class Getter_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 데이터가_없을_때 {
            @Test
            @DisplayName("null을 리턴한다")
            void it_returns_null() {
                assertThat(task.getId())
                        .isNull();

                assertThat(task.getTitle())
                        .isNull();
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 데이터가_있을_때 {
            @Test
            @DisplayName("값을 리턴한다")
           void it_returns_data() {
                task.setId(TEST_ID);
                task.setTitle(TEST_TITLE);

                assertThat(task.getId())
                        .isEqualTo(TEST_ID);

                assertThat(task.getTitle())
                        .isEqualTo(TEST_TITLE);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class Setter_메서드는 {
        @Test
        @DisplayName("값을 저장한다")
        void it_returns_task() {
            Task task = new Task();
            task.setId(TEST_ID);
            task.setTitle(TEST_TITLE);

            assertThat(task.getId())
                    .isNotNull()
                    .isEqualTo(TEST_ID);

            assertThat(task.getTitle())
                    .isNotNull()
                    .isEqualTo(TEST_TITLE);
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class equals_메서드는 {

        Task task1;
        Task task2;

        @BeforeEach
        void setUp() {
            task1 = new Task();
            task2 = new Task();
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 멤버에_같은_값들이_저장된_객체가_주어지면 {
            @Test
            @DisplayName("true를 리턴한다")
            void it_returns_true() {
                task1.setId(TEST_ID);
                task2.setId(TEST_ID);

                task1.setTitle(TEST_TITLE);
                task2.setTitle(TEST_TITLE);

                assertThat(task1).isEqualTo(task2);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 멤버에_다른_값들이_저장된_객체가_주어지면 {
            @Test
            @DisplayName("false를 리턴한다")
            void it_returns_false() {
                task1.setId(TEST_ID);
                task2.setId(TEST_ID + TEST_ID);

                task1.setTitle(TEST_TITLE);
                task2.setTitle(TEST_TITLE + TEST_TITLE);

                assertThat(task1).isNotEqualTo(task2);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class hashCode_메서드는 {

        Task task1;
        Task task2;

        @BeforeEach
        void setUp() {
            task1 = new Task();
            task2 = new Task();
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 멤버에_저장된_값들이_서로_같다면 {
            @Test
            @DisplayName("같은 해시코드를 리턴한다")
            void it_returns_true() {
                task1.setId(TEST_ID);
                task2.setId(TEST_ID);

                task1.setTitle(TEST_TITLE);
                task2.setTitle(TEST_TITLE);

                assertThat(task1).hasSameHashCodeAs(task2);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 멤버에_저장된_값들이_서로_다르다면 {
            @Test
            @DisplayName("다른 해시코드를 리턴한다")
            void it_returns_false() {
                task1.setId(TEST_ID);
                task2.setId(TEST_ID + TEST_ID);

                task1.setTitle(TEST_TITLE);
                task2.setTitle(TEST_TITLE + TEST_TITLE);

                assertThat(task1.hashCode()).isNotSameAs(task2.hashCode());
            }
        }
    }
}
