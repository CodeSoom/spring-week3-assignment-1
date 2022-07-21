package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스")
class TaskTest {
    final Long givenId = 1L;
    final String givenTitle = "BJP";

    @Nested
    @DisplayName("getId 메소드는")
    class Describe_get_id {
        Task task = new Task(givenId, givenTitle);

        @Nested
        @DisplayName("작업이 식별자를 가지고 있을 때")
        class Context_when_Task_has_id {
            @Test
            @DisplayName("작업의 식별자를 리턴한다")
            void it_returns_id() {
                assertThat(task.getId()).isEqualTo(givenId);
            }
        }
    }

    @Nested
    @DisplayName("change 메소드는")
    class Describe_set_title {
        @Nested
        @DisplayName("제목이 주어질 때")
        class Context_with_title {
            final String givenToChangeTitle = "변경할 제목";

            @Test
            @DisplayName("작업에 제목을 할당하고 작업을 리턴한다")
            void it_assignment_title_to_task() {
                Task task = new Task(givenId, givenTitle);

                assertThat(task.changeTitle(givenToChangeTitle)).isEqualTo(new Task(givenId, givenToChangeTitle));
            }
        }
    }

    @Nested
    @DisplayName("getTitle 메소드는")
    class Describe_get_title {
        @Nested
        @DisplayName("작업에 제목이 주어졌을 때")
        class Context_with_title {
            @Test
            @DisplayName("작업의 제목을 리턴한다.")
            void it_returns_title() {
                Task task = new Task(givenId, givenTitle);

                assertThat(task.getTitle()).isEqualTo(givenTitle);
            }
        }
    }

    @Nested
    @DisplayName("equals 메소드는")
    class Describe_equals {
        Task task = new Task(givenId, givenTitle);
        @Nested
        @DisplayName("주어진 작업과 주소값이 같다면")
        class Context_when_equals_address {
            @Test
            @DisplayName("true를 리턴한다")
            void It_returns_true() {
                Task task1 = task;

                assertThat(task.equals(task1)).isTrue();
            }
        }

        @Nested
        @DisplayName("주어진 작업이 null이거나 클래스 타입이 같지 않다면")
        class Context_with_null_and_not_equal_class {
            class MiniTask extends Task {
                public MiniTask(Long givenId, String givenTitle) {
                    super(givenId, givenTitle);
                }
            }

            @Test
            @DisplayName("false를 리턴한다")
            void It_returns_false() {
                Task task1 = null;
                MiniTask task2 = new MiniTask(givenId, givenTitle);

                assertThat(task.equals(task1)).isFalse();
                assertThat(task.equals(task2)).isFalse();
            }
        }

        @Nested
        @DisplayName("주어진 작업과 식별자와 제목이 같으면")
        class Context_with_task {
            @Test
            @DisplayName("true를 리턴한다")
            void It_returns_true() {
                Object task1 = new Task(givenId, givenTitle);

                assertThat(task.equals(task1)).isTrue();
            }
        }
    }

    
}
