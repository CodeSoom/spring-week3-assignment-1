package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void createTaskTest() {
        assertThat(task).isNotNull();
    }

    @Test
    void TaskSetIdTest() {
        task.setId(100L);
        assertThat(task.getId()).isEqualTo(100L);
    }

    @Test
    void TaskSetTitleTest() {
        task.setTitle("Update_Title");
        assertThat(task.getTitle()).isEqualTo("Update_Title");
    }

}
