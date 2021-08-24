package com.codesoom.assignment.application;

import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 할 일을 관리하는 서비스 영역.
 */

@Service
public class TaskService {
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private static Long sequence = 0L;

    private final IdGenerator<Long> idGenerator;

    public TaskService(IdGenerator<Long> idGenerator) {
        this.idGenerator = idGenerator;
    }

    public void clear() {
        tasks.clear();
        sequence = 0L;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task getTask(Long id) {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException(id);
        }

        return tasks.get(id);
    }

    public synchronized Task createTask(Task source) {
        Task task = new Task();
        task.setId(generateId());
        task.setTitle(source.getTitle());

        tasks.put(task.getId(), task);

        return task;
    }

    public synchronized Task updateTask(Long id, Task source) {
        Task task = getTask(id);
        task.setTitle(source.getTitle());

        return task;
    }

    public Task deleteTask(Long id) {
        Task task = getTask(id);
        tasks.remove(id, task);

        return task;
    }

    private Long generateId() {
        sequence = idGenerator.generate(sequence);
        return sequence;
    }
}
