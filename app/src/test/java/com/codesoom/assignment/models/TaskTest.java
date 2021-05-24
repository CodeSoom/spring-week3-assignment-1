package com.codesoom.assignment.models;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Task의 단위 테스트")
class TaskTest {

    @Nested
    @DisplayName("toString 메소드는")
    class Describe_toString {

        @Nested
        @DisplayName("만약 메서드를 호출한다면")
        class Context_call_toString {

            private Task task = new Task();
            private Long taskId = 1L;
            private String title = "Test Title";


            @BeforeEach
            void setUp() {
                task.setId(taskId);
                task.setTitle(title);
            }

            @Test
            @DisplayName("필드들을 특정 포멧으로 문자열화하여 반환합니다.")
            void it_return_fields_to_string() {
                String taskToString = task.toString();
                Assertions.assertThat(taskToString).isEqualTo("Task{id=1, title=Test Title}");
            }

        }

    }

}
