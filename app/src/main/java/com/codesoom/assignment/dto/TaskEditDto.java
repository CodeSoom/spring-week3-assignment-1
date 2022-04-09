package com.codesoom.assignment.dto;

import org.springframework.util.StringUtils;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * 할 일 수정에 필요한 데이터를 정의합니다.
 */
public final class TaskEditDto {

    private final TaskDto taskDto;

    @ConstructorProperties({"title"})
    public TaskEditDto(final String title) {
        this.taskDto = new TaskDto(title);
    }

    public String getTitle() {
        return taskDto.getTitle();
    }

    public void validate() {
        if (!StringUtils.hasText(taskDto.getTitle())) {
            throw new IllegalArgumentException("할 일을 입력해 주세요.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEditDto that = (TaskEditDto) o;
        return Objects.equals(taskDto, that.taskDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskDto);
    }
}
