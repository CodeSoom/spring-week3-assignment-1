package com.codesoom.assignment.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void getMessage() {
        String message = "Error";
        ErrorResponse errorResponse = new ErrorResponse(message);

        assertThat(errorResponse.getMessage()).isEqualTo(message);
    }
}