package com.codesoom.assignment.dto;

import com.codesoom.assignment.models.Task;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * 할 일 등록에 필요한 데이터를 정의합니다.
 */
public final class TaskSaveDto {

    private final TaskDto taskDto;

    @ConstructorProperties({"title"})
    public TaskSaveDto(String title) {
        this.taskDto = new TaskDto(title);
    }

    public String getTitle() {
        return taskDto.getTitle();
    }

    /**
     * 할 일 엔티티로 변환해 리턴합니다.
     */
    public Task toEntity() {
        return new Task(getTitle());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskSaveDto that = (TaskSaveDto) o;
        return Objects.equals(taskDto, that.taskDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskDto);
    }
}
