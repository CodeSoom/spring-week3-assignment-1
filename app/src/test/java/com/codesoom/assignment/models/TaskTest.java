package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Task 클래스")
class TaskTest {

    @Nested
    @DisplayName("만약 유효한 title이 주어진다면")
    class ContextWithValidTitle {
        @Test
        @DisplayName("해당 Task에 title을 설정할 수 있다")
        void itSetsTitleToTask () {
            Task task = new Task();
            task.setTitle("task");

            assertThat(task.getTitle()).isEqualTo("task");
        }
    }

    @Nested
    @DisplayName("만약 유효한 id가 주어진다면")
    class ContextWithValidId {
        @Test
        @DisplayName("해당 Task에 id를 설정할 수 있다")
        void itSetsIdToTask() {
            Task task = new Task();
            task.setId(1L);

            assertThat(task.getId()).isEqualTo(1L);
        }
    }
}
