package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task 클래스")
class TaskTest {

    Task task;
    final long TASK_ID = 0L;
    final long UPDATE_ID = 1L;
    final String TASK_TITLE = "Get Sleep";
    final String UPDATE_TITLE = "Do Study";

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
    }

    @Nested
    @DisplayName("getId() 메서드는")
    class Describe_getId {
        @Nested
        @DisplayName("task가 존재한다면")
        class Context_task_exist {
            @Test
            @DisplayName("task의 id를 반환한다")
            void it_return_task_id() {
                assertThat(task.getId()).isEqualTo(TASK_ID);
            }
        }
    }

    @Nested
    @DisplayName("setId() 메서드는")
    class Describe_setId {
        @Nested
        @DisplayName("task가 존재한다면")
        class Context_task_exist {
            @Test
            @DisplayName("task의 id를 변경한다")
            void it_set_task_id() {
                task.setId(UPDATE_ID);
                assertThat(task.getId()).isEqualTo(UPDATE_ID);
            }
        }
    }

    @Nested
    @DisplayName("getTitle() 메서드는")
    class Describe_getTitle {
        @Nested
        @DisplayName("task가 존재한다면")
        class Context_task_exist {
            @Test
            @DisplayName("task의 title를 반환한다")
            void it_return_task_id() {
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }
        }
    }

    @Nested
    @DisplayName("setTitle() 메서드는")
    class Describe_setTitle {
        @Nested
        @DisplayName("task가 존재한다면")
        class Context_task_exist {
            @Test
            @DisplayName("task의 title을 변경한다")
            void it_set_task_id() {
                task.setTitle(UPDATE_TITLE);
                assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
            }
        }
    }

}
