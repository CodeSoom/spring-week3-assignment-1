package com.codesoom.assignment.models;

public class Task {
    private final Long id;
    private final String title;

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Task updateTitle(String title) {
        return new Task(this.id, title);
    }
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}
