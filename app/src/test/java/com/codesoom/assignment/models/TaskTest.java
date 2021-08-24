package com.codesoom.assignment.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    @Test
    @DisplayName("할 일 생성")
    void createTask() {
        // given
        long id = 1L;
        String title = "할 일";

        // when
        Task task = new Task(id, title);

        // then
        assertThat(task).isEqualTo(new Task(id, title));
    }

    @Test
    @DisplayName("할 일 Equals And HashCode 테스트")
    void taskEqualsAndHashCode() {
        EqualsVerifier.simple().forClass(Task.class).verify();
    }
}
