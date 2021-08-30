package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class TaskServiceQueries {
    private final HashMap<Long, Task> tasks;

    public TaskServiceQueries(HashMap<Long, Task> tasks) {
        this.tasks = tasks;
    }

    public Collection<Task> all() {
        return tasks.values();
    }

    public Task details(Long id) {
        return Optional.ofNullable(tasks.get(id))
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
