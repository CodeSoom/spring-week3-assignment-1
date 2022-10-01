package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @DisplayName("UpdateTitle Method")
    @Nested
    class Describe_updateTitle {
        @DisplayName("if an updated task is given")
        @Nested
        class Context_with_update_title {

           private final Task task = new Task(1L, "title");

            @Test
            @DisplayName("returns an updated Task")
            void it_returns_updated_task() {
                Task updatedTitle = task.updateTitle("title2");
                //then
                assertThat(updatedTitle.getTitle()).isNotEqualTo(task.getTitle());
                assertThat(updatedTitle.getId()).isEqualTo(task.getId());
            }
        }
    }

    @DisplayName("GetId method")
    @Nested
    class Describe_getId {
        @DisplayName("if an valid id is given")
        @Nested
        class Context_with_valid_id {
            private Task task;
            private final Long id1 = 1L;

            @BeforeEach
            void setup() {
                task = new Task(id1, "title");
            }

            @Test
            @DisplayName("returns an id")
            void it_returns_task() {
                Long getId = task.getId();
                assertThat(id1).isEqualTo(getId);
            }
        }
    }
}
