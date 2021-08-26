package com.codesoom.assignment.models;

import java.util.Objects;

public class Task {

    private Long id;

    private String title;

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Task() {

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

    /**
     * 할 일 객체를 JSON 문자로 변환합니다.
     * @return 변환된 JSON 문자
     */
    public String stringify() {
        return String.format("{\"id\":%s,\"title\":\"%s\"}", id, title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title);
    }

}
