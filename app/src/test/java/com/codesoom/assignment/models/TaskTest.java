package com.codesoom.assignment.models;

import com.codesoom.assignment.utils.RandomTitleGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskTest 클래스의")
class TaskTest {

    @Nested
    @DisplayName("equals() 메소드는")
    class Describe_equasl_method {

        private Task original;
        private Task argument;

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 메소드를_호출하는_객체와_인자로_주어지는_객체가_동일한_경우 {

            @BeforeEach
            void setUp() {
                original = argument = new Task(1L, RandomTitleGenerator.getRandomTitle());
            }

            @Test
            @DisplayName("true를 리턴한다.")
            void it_returns_true() {
                assertThat(original.equals(argument)).isTrue();
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 인자로_주어지는_객체가_null일_경우 {

            @BeforeEach
            void setUp() {
                original = new Task(1L, RandomTitleGenerator.getRandomTitle());
                argument = null;
            }

            @Test
            @DisplayName("false를 리턴한다.")
            void it_returns_false() {
                assertThat(original.equals(argument)).isFalse();
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 메소드를_호출하는_객체와_인자로_주어지는_객체가_다른_타입일_경우 {

            private Object argument;

            @BeforeEach
            void setUp() {
                original = new Task(1L, RandomTitleGenerator.getRandomTitle());
                argument = new Object();
            }

            @Test
            @DisplayName("fasle를 반환한다.")
            void it_returns_false() {
                assertThat(original.equals(argument)).isFalse();
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 메소드를_호출하는_객체의_id와_인자로_주어지는_객체의_id가_다를_경우 {

            @BeforeEach
            void setUp() {
                final String title = RandomTitleGenerator.getRandomTitle();
                original = new Task(1L, title);
                argument = new Task(2L, title);
            }

            @Test
            @DisplayName("false를 반환한다.")
            void it_returns_false() {
                assertThat(original.equals(argument)).isFalse();
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 메소드를_호출하는_객체의_title과_인자로_주어지는_객체의_title이_다를_경우 {

            @BeforeEach
            void setUp() {
                original = new Task(1L, RandomTitleGenerator.getRandomTitle());
                argument = new Task(1L, RandomTitleGenerator.getRandomTitleDifferentFrom(original.getTitle()));
            }

            @Test
            @DisplayName("false를 리턴한다.")
            void it_returns_false() {
                assertThat(original.equals(argument)).isFalse();
            }
        }

        @Nested
        @DisplayName("메소드를 호출하는 객체의 id, title과 인자의 id, title이 서로 같을 경우")
        class Context_when_id_and_title_of_task_calling_method_is_equals_to_them_of_argument {

            @BeforeEach
            void setUp() {
                final Long id = 1L;
                final String title = RandomTitleGenerator.getRandomTitle();

                original = new Task(id, title);
                argument = new Task(id, title);
            }

            @Test
            @DisplayName("true를 리턴한다.")
            void it_returns_true() {
                assertThat(original.equals(argument)).isTrue();
            }
        }
    }
}
