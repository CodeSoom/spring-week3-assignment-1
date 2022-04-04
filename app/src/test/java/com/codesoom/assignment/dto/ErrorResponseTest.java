package com.codesoom.assignment.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    private static final String ERROR_MESSAGE = "알 수 없는 오류가 발생하였습니다.";

    @Test
    @DisplayName("오류메시지 생성 후 확인하기")
    void errorMessage() {
        ErrorResponse errorResponse = new ErrorResponse(ERROR_MESSAGE);

        assertThat(errorResponse.getMessage()).isEqualTo(ERROR_MESSAGE);
    }
}
