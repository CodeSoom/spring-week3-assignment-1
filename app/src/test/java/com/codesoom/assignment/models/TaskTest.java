package com.codesoom.assignment.models;

import com.codesoom.assignment.NotProperTaskFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskTest {

    private static final String TASK_TITLE = "To complete CodeSoom assignment";

    private Task task;

    @BeforeEach
    void init() {
        task = new Task();
    }

    @Test
    @DisplayName("task 번호 세팅하고 조회하기")
    void taskIdTest() {
        task.setId(1L);

        assertThat(task.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("task 제목 세팅하고 조회하기")
    void taskTitleTest() {
        task.setTitle(TASK_TITLE);

        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }


    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("할일의 제목이 없거나 빈값일 경우 오류발생")
    void taskValidationTest(String title) {
        task.setTitle(title);

        assertThatThrownBy(() -> task.validation())
                .isInstanceOf(NotProperTaskFormatException.class);
    }
}
