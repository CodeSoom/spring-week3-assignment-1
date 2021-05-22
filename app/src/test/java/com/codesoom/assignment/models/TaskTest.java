package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task class")
class TaskTest {
    private final Long ID = 1L;
    private final String TITLE = "Test Task";

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
                task.setId(ID);
                task.setTitle(TITLE);

                assertThat(task).isNotNull();
                assertThat(task.getId()).isEqualTo(ID);
                assertThat(task.getTitle()).isEqualTo(TITLE);
            }
        }
    }

    @Nested
    @DisplayName("equals method")
    class DescribeEquals {
        @BeforeEach
        void prepared() {
            task.setId(ID);
            task.setTitle(TITLE);
        }

        @Nested
        @DisplayName("when same task id and title")
        class ContextWithEqualTask {
            @Test
            @DisplayName("It returns true")
            void equals() {
                Task otherTask = new Task();
                otherTask.setId(ID);
                otherTask.setTitle(TITLE);

                assertThat(task.equals(otherTask)).isTrue();
            }
        }

        @Nested
        @DisplayName("when different task id and title")
        class ContextWithNotEqualTask {
            @Test
            @DisplayName("It returns false")
            void equals() {
                Task otherTask1 = new Task();
                otherTask1.setId(ID + 1);
                otherTask1.setTitle(TITLE);

                Task otherTask2 = new Task();
                otherTask2.setId(ID);
                otherTask2.setTitle(TITLE + "OTHER");

                Task otherTask3 = new Task();
                otherTask3.setId(ID + 99);
                otherTask3.setTitle(TITLE + "OTHER");

                assertThat(task.equals(otherTask1)).isFalse();
                assertThat(task.equals(otherTask2)).isFalse();
                assertThat(task.equals(otherTask3)).isFalse();
            }
        }

        @Nested
        @DisplayName("when different type object")
        class ContextWithUnknownObject {
            @Test
            @DisplayName("It returns false")
            void equals() {
                String content = "String Object";
                Long number = 1L;
                List list = new ArrayList<Object>();
                Map map = new HashMap<Object, Object>();

                assertThat(task.equals(content)).isFalse();
                assertThat(task.equals(number)).isFalse();
                assertThat(task.equals(list)).isFalse();
                assertThat(task.equals(map)).isFalse();
            }
        }
    }
}
