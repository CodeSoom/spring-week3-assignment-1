package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 모델 테스트")
class TaskTest {

    public static final long FIRST_ID = 1L;

    @DisplayName("할 일을 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"t1", "test2", "testtest3"})
    void create(String title) {
        Task newTask1 = new Task();
        newTask1.setId(FIRST_ID);
        newTask1.setTitle(title);

        assertThat(newTask1.getTitle()).isNotEqualTo(title);
        assertThat(newTask1.getId()).isEqualTo(FIRST_ID);
    }
}
