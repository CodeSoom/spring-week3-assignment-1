package com.codesoom.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TaskIdGeneratorTest {

    @Test
    @DisplayName("할 일 아이디를 1씩 증가시키고 반환한다.")
    void generate() {
        TaskIdGenerator taskIdGenerator = new TaskIdGenerator();

        assertThat(taskIdGenerator.generate()).isEqualTo(1L);
        assertThat(taskIdGenerator.generate()).isEqualTo(2L);
        assertThat(taskIdGenerator.generate()).isEqualTo(3L);
    }
}
