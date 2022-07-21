package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TaskRepository {
    private final Map<Long, Task> taskMap = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    /**
     * 작업을 추가합니다.
     *
     * @param title 제목
     * @return 작업
     */
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

    public Task remove(Long id) {
        return taskMap.remove(id);
    }
}
