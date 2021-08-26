package com.codesoom.assignment.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Nested
@DisplayName("ErrorResponse 클래스")
class ErrorResponseTest {

    private final String ERROR_MSG =  "에러 발생";

    @Nested
    @DisplayName("ErrorResponse 메소드는")
    class Describe_errorResponse {

        @Nested
        @DisplayName("에러가 발생한다면")
        class Context_error_occur {

            @Test
            @DisplayName("에러 메세지를 출력한다")
            void It_error_msg_print() {

                ErrorResponse errorResponse = new ErrorResponse(ERROR_MSG);
                assertEquals(errorResponse.getMessage(), ERROR_MSG);

            }

        }

    }

}
