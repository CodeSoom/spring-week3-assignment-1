package com.codesoom.assignment.domain;

public class Task {

    private Long id;
    private String title;

    public Task() {

    }

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public boolean hasValidTitle() {
        return this.title != null;
    }
}
