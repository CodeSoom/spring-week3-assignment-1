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
}
