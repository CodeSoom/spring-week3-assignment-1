package com.codesoom.assignment.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class Task {
    private Long id;
    private String title;

    @Builder
    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
