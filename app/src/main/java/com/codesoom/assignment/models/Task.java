package com.codesoom.assignment.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private Long id;

    private String title;

    public Task() {
    }

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
