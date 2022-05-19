package com.codesoom.assignment.models;

import com.google.common.base.Objects;

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

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equal(id, task.id) && Objects.equal(title, task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, title);
    }
}
