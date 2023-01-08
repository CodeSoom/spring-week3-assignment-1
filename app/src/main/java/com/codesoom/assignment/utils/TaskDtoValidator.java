package com.codesoom.assignment.utils;

import com.codesoom.assignment.exceptions.InvalidTaskDtoTitleException;
import com.codesoom.assignment.models.TaskDto;

public class TaskDtoValidator {

    public static void validate(TaskDto dto) {
        final String title = dto.getTitle();
        if (title == null || title.isBlank()) {
            throw new InvalidTaskDtoTitleException(title);
        }
    }
}
