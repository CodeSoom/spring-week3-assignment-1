package com.codesoom.assignment.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class Task {
    private Long id;
    private String title;
    private LocalDateTime endTime = LocalDateTime.MAX;

    public Task() {}

    @Builder
    public Task(Long id, String title, LocalDateTime endTime) {
        this.id = id;
        this.title = title;

        if (endTime != null) {
            this.endTime = endTime;
        }
    }
}
