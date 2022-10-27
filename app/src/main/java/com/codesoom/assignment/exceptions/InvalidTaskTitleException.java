package com.codesoom.assignment.exceptions;

public class InvalidTaskTitleException extends IllegalArgumentException {
    public InvalidTaskTitleException(String invalidTitle) {
        super(invalidTitle + "은 title로 허용되지 않습니다. null 또는 공백이 아닌 문자열을 입력해주세요.");
    }
}
