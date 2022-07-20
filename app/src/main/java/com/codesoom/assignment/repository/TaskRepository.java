package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TaskRepository {
    private Long sequence = 0L;
    private final Map<Long, Task> taskMap = new HashMap<>();

    public Task add(String title) {
        Task task = new Task(sequence, title);
        taskMap.put(sequence++, task);
        return task;
    }

    public Optional<Task> get(Long id) {
        return Optional.ofNullable(taskMap.get(id));
    }

    public List<Task> getAll() {
        return new ArrayList<>(taskMap.values());
    }
}
