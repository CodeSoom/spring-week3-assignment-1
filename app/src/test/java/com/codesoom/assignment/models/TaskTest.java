package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private Task task;
    private Task titleNullTask;
    private Task idNullTask;
    @BeforeEach
    void setUp() {
        task = new Task(1L, "BJP");
        titleNullTask =  new Task(1L, null);
        idNullTask = new Task(null, "BJP");
    }

    @Test
    @DisplayName("작업에 식별자를 주고 작업이 주어진 식별자를 가지고 있는지 확인한다.")
    void giveTaskIdAndCheckHasGivenId() {
        Task task = new Task();
        task.setId(1L);

        assertThat(task).isEqualTo(titleNullTask);
    }

    @Test
    @DisplayName("작업에 제목을 주었을 때, 해당 제목을 가지고 있어야 한다.")
    void mustHaveTitleWhenGivenTaskWithTitle() {
        Task task = new Task();
        task.setTitle("BJP");

        assertThat(task).isEqualTo(idNullTask);
    }

    @Test
    @DisplayName("식별자를 가진 작업이 주어지고 식별자를 가져올 때, 주어진 식별자를 가지고 있는지 확인한다.")
    void checkHasIdWhenGivenTaskWithId() {
        assertThat(task.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("제목을 가진 작업이 주어지고 제목을 가져올 때, 주어진 제목을 가지고 있는지 확인한다.")
    void checkHasTitleWhenGivenTaskWithTitle() {
        assertThat(task.getTitle()).isEqualTo("BJP");
    }
}
