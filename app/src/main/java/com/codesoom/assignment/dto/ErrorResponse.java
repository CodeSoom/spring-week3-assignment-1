package com.codesoom.assignment.dto;

/**
 * 에러에 대한 정보를 저장하고 처리합니다.
 */
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    /**
     * 에러 메세지를 리턴합니다.
     * @return 에러메세지
     */
    public String getMessage() {
        return message;
    }
}
