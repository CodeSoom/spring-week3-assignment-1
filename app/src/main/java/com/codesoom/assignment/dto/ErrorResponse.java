package com.codesoom.assignment.dto;

/**
 * json형식 에러메시지 전달에 사용
 */
public class ErrorResponse {
    private String message;

    /**
     * @param message 에러 메시지
     */
    public ErrorResponse(String message) {
        this.message = message;
    }

    /**
     * 에러메시지를 리턴한다.
     *
     * @return 에러메시지
     */
    public String getMessage() {
        return message;
    }
}
