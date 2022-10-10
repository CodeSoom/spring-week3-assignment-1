package com.codesoom.assignment.dto;

import java.beans.ConstructorProperties;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskRequestDto)) {
            return false;
        }
        TaskRequestDto that = (TaskRequestDto) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
