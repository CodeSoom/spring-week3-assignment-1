package com.codesoom.assignment.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {

    private final Long id;
    private final String title;

    @JsonCreator
    public Task(@JsonProperty Long id, @JsonProperty String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
