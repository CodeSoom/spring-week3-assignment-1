package com.codesoom.assignment.dto;

import java.util.Objects;

/**
 * 할 일 생성, 수정시 공통으로 필요한 데이터를 정의합니다.
 */
public class TaskDto {

    private String title;

    public TaskDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDto taskDto = (TaskDto) o;
        return Objects.equals(title, taskDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
