package com.codesoom.assignment.models;

public class Task {
    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public Task() {
    }

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
