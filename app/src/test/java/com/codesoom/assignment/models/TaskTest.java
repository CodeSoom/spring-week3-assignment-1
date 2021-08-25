package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {



    @Test
    @DisplayName("객체 생성 테스트")
    void create() {

        Task taskA = new Task();
        taskA.setId(1L);
        taskA.setTitle("taskA");

        assertThat(taskA.getTitle()).isEqualTo("taskA");
        assertThat(taskA.getId()).isEqualTo(1L);

    }

}
