package com.codesoom.assignment.dto;

/**
 *  할 일 수정에 필요한 데이터를 정의합니다.
 */
public class TaskEditDto {

    private String title;

    public TaskEditDto() {
    }

    public TaskEditDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
