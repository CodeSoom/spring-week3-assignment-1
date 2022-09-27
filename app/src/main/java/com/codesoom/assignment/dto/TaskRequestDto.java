package com.codesoom.assignment.dto;

import java.beans.ConstructorProperties;

/** 할 일의 생성과 수정 요청에 사용된다 */
public final class TaskRequestDto {
    private final String title;

    @ConstructorProperties({"title"})
    public TaskRequestDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
