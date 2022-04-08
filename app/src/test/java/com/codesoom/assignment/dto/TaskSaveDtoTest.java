package com.codesoom.assignment.dto;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskSaveDto 클래스")
class TaskSaveDtoTest {

    private static final String TASK_TITLE = "TITLE";

    @Nested
    @DisplayName("toEntity 메소드는")
    class Describe_toEntity {

        @Nested
        @DisplayName("할 일 등록에 필요한 데이터로")
        class Context_this {

            final TaskSaveDto taskSaveDto = new TaskSaveDto(TASK_TITLE);

            Task subject() {
                return taskSaveDto.toEntity();
            }

            @Test
            @DisplayName("할 일 객체를 생성하고 리턴한다.")
            void it_return_task() {
                assertThat(subject().getTitle()).isEqualTo(TASK_TITLE);
            }
        }
    }
}