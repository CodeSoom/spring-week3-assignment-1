package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task class")
class TaskTest {
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Nested
    @DisplayName("getter setter method")
    class DescribeGetterSetter {
        @Nested
        @DisplayName("without id and title")
        class ContextWithoutValues {
            @Test
            @DisplayName("It contains empty ID and title")
            void taskWithoutValues() {
                assertThat(task).isNotNull();
                assertThat(task.getId()).isNull();
                assertThat(task.getTitle()).isNull();
            }
        }

        @Nested
        @DisplayName("with id and title")
        class ContextWithValues {
            @Test
            @DisplayName("It contains ID and title")
            void taskWithValues() {
                final Long id = 1L;
                final String title = "TEST TITLE";

                task.setId(id);
                task.setTitle("TEST TITLE");

                assertThat(task).isNotNull();
                assertThat(task.getId()).isEqualTo(id);
                assertThat(task.getTitle()).isEqualTo(title);
            }
        }
    }
}
