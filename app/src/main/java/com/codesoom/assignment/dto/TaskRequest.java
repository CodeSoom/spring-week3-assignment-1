package com.codesoom.assignment.dto;

import com.codesoom.assignment.exceptions.BadRequestException;

public class TaskRequest {
    private String title;

    public String getTitle() {
        return title;
    }

    public void checkTitle() {
        if(this.title.isBlank()) {
            throw new BadRequestException();
        }
    }
}
