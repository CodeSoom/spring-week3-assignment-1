package com.codesoom.assignment.models;

import com.codesoom.assignment.exceptions.EmptyTitleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task 클래스")
class TaskTest {
    @Nested
    @DisplayName("setTitle 메서드는")
    class Describe_setTitle {
        @Nested
        @DisplayName("null 이나 빈 값을 받았을 때")
        class Context_with_empty_title {
            String titleNull = null;
            String titleEmpty = "";

            @Test
            @DisplayName("EmptyTitleException 을 던진다.")
            void it_throws_empty_title_exception() {
                Task task = new Task();

                assertThatThrownBy(() -> task.setTitle(titleNull))
                        .isInstanceOf(EmptyTitleException.class);

                assertThatThrownBy(() -> task.setTitle(titleEmpty))
                        .isInstanceOf(EmptyTitleException.class);
            }
        }
    }
}