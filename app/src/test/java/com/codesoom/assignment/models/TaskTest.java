package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TEST_클래스 {
    private final Long TEST_ID = 1L;
    private final String TEST_TITLE = "Test!";

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class Setter_메서드_사용_시 {
        @Test
        @DisplayName("Getter 메서드로 세팅한 데이터를 반환한다")
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
}
