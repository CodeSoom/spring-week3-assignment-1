package com.codesoom.assignment.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ErrorResponseTest {

    @Test
    void getMessage() {
        String message = "MESSAGE";

        ErrorResponse response = new ErrorResponse(message);

        assertThat(response.getMessage()).isEqualTo(message);
    }
}
