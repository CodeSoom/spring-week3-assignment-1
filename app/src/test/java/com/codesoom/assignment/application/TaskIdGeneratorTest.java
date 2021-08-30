package com.codesoom.assignment.application;

import com.codesoom.assignment.exceptions.NotSupportedIdTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("할 일 아이디 생성기 테스트")
class TaskIdGeneratorTest {
    private final IdGenerator<Long> idGenerator = new TaskIdGenerator();


    @DisplayName("항상 전달한 인수값에 1을 더하여 반환합니다.")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void idGenerate(Long sequence) {
        final Long newId = idGenerator.generate(sequence);

        assertThat(newId).isEqualTo(sequence + 1);
    }

    @DisplayName("타입이 다른 인수값을 전달하면 예외가 발생합니다.")
    @ParameterizedTest
    @MethodSource("provideInvalidSource")
    void idGenerateInvalid(Object source) {
        assertThatThrownBy(()-> idGenerator.generate(source))
                .isInstanceOf(NotSupportedIdTypeException.class);
    }

    public static Stream<Arguments> provideInvalidSource() {
        return Stream.of(
                Arguments.of(1.0),
                Arguments.of('1'),
                Arguments.of(""),
                Arguments.of("abc"),
                Arguments.of(-1)
        );
    }


}
