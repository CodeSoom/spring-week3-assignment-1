package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private final Long TEST_ID = 1L;
    private final String TEST_TITLE = "Test!";

    @Test
    void validGetterAndSetter() {
        Task task = new Task();

        Long oldId = task.getId();
        String oldTitle = task.getTitle();

        task.setId(TEST_ID);
        task.setTitle(TEST_TITLE);

        Long newId = task.getId();
        String newTitle = task.getTitle();

        assertThat(newId).isNotEqualTo(oldId)
                .isEqualTo(TEST_ID);

        assertThat(newTitle).isNotEqualTo(oldTitle)
                .isEqualTo(TEST_TITLE);
    }
}
