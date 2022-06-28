package com.codesoom.assignment.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testGetMessage() {
        String expected = "test message";

        ErrorResponse errorResponse = new ErrorResponse(expected);

        assertThat(errorResponse.getMessage()).isEqualTo(expected);
    }
}