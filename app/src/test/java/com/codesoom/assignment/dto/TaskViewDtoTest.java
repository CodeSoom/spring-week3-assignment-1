package com.codesoom.assignment.dto;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("TaskViewDto 클래스")
class TaskViewDtoTest {

    private static final String TASK_TITLE = "TITLE";

    @Nested
    @DisplayName("from 정적 메소드는")
    class Describe_from {

        @Nested
        @DisplayName("할 일 객체를 받아")
        class Context_givenTask {

            final Task givenTask = new Task(1L, TASK_TITLE);

            TaskViewDto subject() {
                return TaskViewDto.from(givenTask);
            }

            @Test
            @DisplayName("전송에 필요한 할 일 데이터를 리턴한다.")
            void it_return_taskViewDto() {
                assertAll(
                        () -> assertThat(subject().getId()).isEqualTo(givenTask.getId()),
                        () -> assertThat(subject().getTitle()).isEqualTo(TASK_TITLE)
                );
            }
        }
    }
}
