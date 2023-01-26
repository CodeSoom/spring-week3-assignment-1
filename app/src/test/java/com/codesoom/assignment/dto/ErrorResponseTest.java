package com.codesoom.assignment.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

  @Test
  void constructor_hasMessage() {
    String errorMessage = "This is an error message";
    ErrorResponse errorResponse = new ErrorResponse(errorMessage);
    assertThat(errorResponse.getMessage()).isEqualTo(errorMessage);
  }
}