package com.codesoom.assignment.dto;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskSaveDto 클래스")
class TaskSaveDtoTest {

    private static final String TASK_TITLE = "TITLE";

    @Nested
    @DisplayName("toEntity 메소드는")
    class Describe_toEntity {

        @Nested
        @DisplayName("할 일 내용이 정상적으로 있다면")
        class Context_validData {

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

        @Nested
        @DisplayName("할 일 내용이")
        class Context_titleValue {

            TaskSaveDto taskSaveDto;

            void subject(String title) {
                taskSaveDto = new TaskSaveDto(title);
                taskSaveDto.toEntity();
            }

            @ParameterizedTest(name = "\"{0}\" 이라면 예외를 던진다.")
            @NullAndEmptySource
            @ValueSource(strings = {" "})
            void it_throw_exception(String given) {
                assertThatThrownBy(() -> subject(given))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
