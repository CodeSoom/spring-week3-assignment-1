package com.codesoom.assignment.models;

import com.codesoom.assignment.exceptions.EmptyTitleException;
import org.springframework.util.StringUtils;

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
        if (StringUtils.isEmpty(title)) {
           throw new EmptyTitleException();
        }

        this.title = title;
    }
}
