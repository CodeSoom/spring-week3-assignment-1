package com.codesoom.assignment.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("TaskTest")
class TaskTest {

    @Test
    @DisplayName("Task 객체에 Id와 Title 값이 주어지면 두 값 모두 반환한다.")
    void getTaskWithResultOfIdAndTitle() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("책 읽기");

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("책 읽기");
    }

}
