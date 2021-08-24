package com.codesoom.assignment.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("할 일 아이디 생성기 테스트")
class TaskIdGeneratorTest {
    private final IdGenerator<Long> idGenerator = new TaskIdGenerator();


    @DisplayName("항상 전달한 인수값에 1을 더하여 반환한다.")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void idGenerate(Long sequence) {
        final Long newId = idGenerator.generate(sequence);
        
        assertThat(newId).isEqualTo(sequence + 1);
    }


}
