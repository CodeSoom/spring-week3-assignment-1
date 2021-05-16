package com.codesoom.assignment.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ErrorResponse 클래스")
public class ErrorResponseTest {

    @DisplayName("getMessage는 message를 반환한다")
    @Test
    void createErrorResponse() {
        String errorMessage = "test error message";
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);

        assertThat(errorResponse.getMessage()).isEqualTo(errorMessage);
    }

}
