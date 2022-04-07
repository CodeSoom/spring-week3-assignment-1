package com.codesoom.assignment.dto;

import com.codesoom.assignment.models.Task;

/**
 * 할 일 등록에 필요한 데이터를 정의합니다.
 */
public class TaskSaveDto {

    private String title;

    public TaskSaveDto() {
    }

    public TaskSaveDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 할 일 엔티티로 변환해 리턴합니다.
     */
    public Task toEntity() {
        return new Task(title);
    }
}
