package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("Task 클래스")
class TaskTest {
    final Long givenId = 1L;
    final String givenTitle = "BJP";

    @Nested
    @DisplayName("setId 메소드는")
    class Describe_set_id {
        @Nested
        @DisplayName("식별자가 주어진다면")
        class Context_with_id {
            @Test
            @DisplayName("작업의 식별자를 주어진 식별자로 할당한다.")
            void it_assignment_id_to_task() {
                Task task = new Task();
                task.setId(givenId);

                assertThat(task.getId()).isEqualTo(givenId);
            }
        }
    }

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
    @DisplayName("setTitle 메소드는")
    class Describe_set_title {
        @Nested
        @DisplayName("제목이 주어질 때")
        class Context_with_title {
            @Test
            @DisplayName("작업에 제목을 할당한다")
            void it_assignment_title_to_task() {
                Task task = new Task();
                task.setTitle(givenTitle);

                assertThat(task.getTitle()).isEqualTo(givenTitle);
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
