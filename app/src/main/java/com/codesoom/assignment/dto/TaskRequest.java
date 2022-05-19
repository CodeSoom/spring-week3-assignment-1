package com.codesoom.assignment.dto;

public class TaskRequest {
    private String title;

    public TaskRequest() {}

    public TaskRequest(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
