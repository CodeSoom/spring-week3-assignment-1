package com.codesoom.assignment.utils;

import com.codesoom.assignment.exceptions.InvalidTaskDtoTitleException;
import com.codesoom.assignment.exceptions.NullTaskDtoException;
import com.codesoom.assignment.models.TaskDto;

public class TaskDtoValidator {

    public static void validate(TaskDto dto) {
        if (dto == null) {
            throw new NullTaskDtoException();
        }

        validateTitle(dto.getTitle());
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidTaskDtoTitleException(title);
        }
    }
}
