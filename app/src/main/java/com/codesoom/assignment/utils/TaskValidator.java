package com.codesoom.assignment.utils;

import com.codesoom.assignment.exceptions.InvalidTaskTitleException;
import com.codesoom.assignment.exceptions.NullTaskException;
import com.codesoom.assignment.models.Task;

public class TaskValidator {

    public static void validate(Task task) {
        if (task == null) {
            throw new NullTaskException();
        }

        validateTitle(task.getTitle());
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidTaskTitleException(title);
        }
    }
}
