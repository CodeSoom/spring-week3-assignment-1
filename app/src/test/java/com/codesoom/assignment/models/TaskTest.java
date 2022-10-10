package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("할 일 Model 테스트")
class TaskTest {

    private static final String UPDATE_PREFIX = "update_";
    private static final String TEST_TITLE = "test title";

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task(1L, TEST_TITLE);
    }

    @Test
    @DisplayName("새로운 할 일을 정상적으로 생성할 수 있다")
    void testTask() {
    }

    @Test
    @DisplayName("할 일의 id와 title을 조회할 수 있다")
    void getterTest() {
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TEST_TITLE);
    }

    @Test
    @DisplayName("할 일의 title을 업데이트 할 수 있다")
    void updateTask() {
        assertThat(task.getTitle()).isEqualTo(TEST_TITLE);

        task.updateTitle(UPDATE_PREFIX + TEST_TITLE);

        assertThat(task.getTitle()).isEqualTo(UPDATE_PREFIX + TEST_TITLE);
    }
}
