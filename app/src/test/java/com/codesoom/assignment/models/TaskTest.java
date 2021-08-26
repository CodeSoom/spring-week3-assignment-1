package com.codesoom.assignment.models;

import com.codesoom.assignment.constant.TaskConstant;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    @Test
    @DisplayName("할 일 생성")
    void createTask() {
        // when
        Task task = new Task();
        task.setId(TaskConstant.ID);
        task.setTitle(TaskConstant.TITLE);

        // then
        assertThat(task).isEqualTo(new Task(TaskConstant.ID, TaskConstant.TITLE));
    }

    @Test
    @DisplayName("할 일 Equals And HashCode 테스트")
    void taskEqualsAndHashCode() {
        // EqualsVerifier를 사용하여 EqualsAndHashCode 자동 테스트
        EqualsVerifier.simple().forClass(Task.class).verify();
    }
}
