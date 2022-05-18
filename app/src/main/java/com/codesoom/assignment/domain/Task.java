package com.codesoom.assignment.domain;

public class Task {

    private final Long id;
    private String title;

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Task createNewTask(Long id, Task task) {
        return new Task(id, task.title);
    }

    public boolean hasValidTitle() {
        return this.title != null;
    }

    public Task changeTitle(String title) {
        this.title = title;
        return this;
    }

    public String currentTitle() {
        return this.title;
    }
}
