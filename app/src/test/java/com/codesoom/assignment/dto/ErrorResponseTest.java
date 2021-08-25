package com.codesoom.assignment.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ErrorResponseTest 클래스")
class ErrorResponseTest {
    private static final String ERROR_MESSAGE = "error message";

    @Nested
    @DisplayName("getMessage 메소드는")
    class Describe_getMessage {
        ErrorResponse errorResponse;

        @BeforeEach
        void prepareErrorResponse() {
            errorResponse = new ErrorResponse(ERROR_MESSAGE);
        }

        @Test
        @DisplayName("저장된 에러메시지를 리턴합니다")
        void it_returns_ERROR_() {
            assertThat(errorResponse.getMessage()).isEqualTo(ERROR_MESSAGE);
        }
    }
}
