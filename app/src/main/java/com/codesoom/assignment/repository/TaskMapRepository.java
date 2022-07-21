package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TaskMapRepository implements TaskRepository {
    private final Map<Long, Task> taskMap = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    public Task add(String title) {
        Long id = idGenerator.generate();
        Task task = new Task(id, title);
        taskMap.put(id, task);
        return task;
    }

    public Optional<Task> get(Long id) {
        return Optional.ofNullable(taskMap.get(id));
    }

    public Collection<Task> getAll() {
        return taskMap.values();
    }

    public void remove(Long id) {
        taskMap.remove(id);
    }
}
