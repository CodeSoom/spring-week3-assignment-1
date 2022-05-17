package com.codesoom.assignment.models;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskTest 클래스")
class TaskTest {
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class getId_메서드는 {
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 만약_id가_1인_Task_객체가_주어진다면 {
            private Task task;

            @BeforeEach
            void setUp() {
                task = new Task("과제하기");
                task.setId(1L);
            }

            @Test
            @DisplayName("id의 값으로 1을 반환한다")
            void id의_값으로_1을_반환한다() {
                assertThat(task.getId()).isEqualTo(1L);
            }
        }
    }
}
