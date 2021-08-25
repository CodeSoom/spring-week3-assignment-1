package com.codesoom.assignment.models.data;

public class FakeTask {
    private Long id;
    private String title;

    public FakeTask(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
