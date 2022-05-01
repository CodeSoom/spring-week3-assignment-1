package com.codesoom.assignment.models;

import org.springframework.util.StringUtils;

import java.util.Objects;

public class Task {

    private Long id;

    private String title;

    public Task(String title) {
        this(null, title);
    }

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
        validate();
    }

    private void validate() {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("할 일을 입력해 주세요.");
        }
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
