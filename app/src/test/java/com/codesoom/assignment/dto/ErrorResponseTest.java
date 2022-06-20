package com.codesoom.assignment.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {
    static final String ERROR_RES_MSG = "Error Response Test Message";
    ErrorResponse errorResponse = new ErrorResponse(ERROR_RES_MSG);
    @DisplayName("getMessage 동작 테스트")
    @Test
    public void getMessageTest() {
        //given
        //when
        String receivedErrResMsg = errorResponse.getMessage();
        //then
        assertThat(receivedErrResMsg).isEqualTo(ERROR_RES_MSG);
    }

}