package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Task;

public class TaskDto {
    private Long id;
    private String title;

    public TaskDto() {

    }

    public TaskDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Task toTask() {
        return new Task(this.id, this.title);
    }
}
