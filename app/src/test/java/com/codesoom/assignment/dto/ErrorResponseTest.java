package com.codesoom.assignment.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ErrorResponse 클래스")
public class ErrorResponseTest {

    @DisplayName("ErrorResponse 객체를 생성하면 생성자에 입력한 인자를 getMessage로 값을 리턴받을 수 있다")
    @Test
    void createErrorResponse() {
        String errorMessage = "test error message";
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);

        assertThat(errorResponse.getMessage()).isEqualTo(errorMessage);
    }

}
