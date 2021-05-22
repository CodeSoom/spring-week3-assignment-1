package com.codesoom.assignment.models;

import org.springframework.util.StringUtils;

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

    public boolean equals(Object obj) {
        if (!(obj instanceof Task)) {
            return false;
        }
        return id.equals(((Task)obj).getId())
            && title.equals(((Task)obj).getTitle());
    }
}
