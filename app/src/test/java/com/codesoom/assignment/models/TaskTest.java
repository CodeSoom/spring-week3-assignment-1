package com.codesoom.assignment.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("TaskTest")
class TaskTest {

    private Task task;
    private static final String TASK_TITLE = "Testing...";
    private static final Long TASK_ID = 1L;

    @BeforeEach
    void setUp () {
        Task task = new Task();
        task.setTitle(TASK_TITLE);
    }

    @Test @Nested
    @DisplayName("객체에 저장된 Id 값이 주어지면")
    void setId() {
        Task task = new Task();

        Assertions.assertEquals(task.getId(1L), TASK_ID, "주어진 객체를 저장하고, Id 값을 반환한다.");
    }

    @Test @Nested
    @DisplayName("객체에 저장된 Title 값이 주어지면")
    void setTaskTitle() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        Assertions.assertEquals(task.getTitle(), TASK_TITLE, "주어진 객체를 저장하고, Title 값을 반환한다.");

    }
}
