package com.codesoom.assignment.models;

import java.util.Objects;

public class Task {
    private Long id;

    private String title;

    public Task() {
    }

    public Task(Long id, String title) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        Task task = (Task) o;
        return Objects.equals(this.id, task.getId()) && this.title.equals(task.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
