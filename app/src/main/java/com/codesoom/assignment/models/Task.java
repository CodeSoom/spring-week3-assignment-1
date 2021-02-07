package com.codesoom.assignment.models;

import java.util.Objects;

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

    /**
     * 할 일의 값이 같은지 비교합니다
     * @param o 비교 대상
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(getId(), task.getId()) &&
                Objects.equals(getTitle(), task.getTitle());
    }

    /**
     * 할 일의 해쉬값을 리턴합니다.
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle());
    }
}
