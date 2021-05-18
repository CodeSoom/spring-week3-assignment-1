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

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Task)) {
            return false;
        }
        return id.equals(((Task)obj).getId()) &&
                title.equals(((Task)obj).getTitle());
    }

    @Override
    public int hashCode() {
        // id, title이 null를 고려한 hashCode.. 이게.. 맞나..??
        int result = id != null ? id.hashCode() * 31 : 0;
        result += title != null ? title.hashCode() * 31 : 0;
        return result;
    }
}
