package com.codesoom.assignment.application;

import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class TaskService {

    private final Map<Long, Task> taskMap = new HashMap<>();
    private Long newId = 0L;

    public Collection<Task> getTasks() {
        return Collections.unmodifiableCollection(taskMap.values());
    }

    public Task getTask(Long id) {
        if (taskDoesNotExist(id)) {
            throw new TaskNotFoundException(id);
        }

        return taskMap.get(id);
    }

    public Task createTask(Task source) {
        final Task task = new Task(generateId(), source.getTitle());

        taskMap.put(task.getId(), task);

        return task;
    }

    public Task updateTask(Long id, Task source) {
        if (taskDoesNotExist(id)) {
            throw new TaskNotFoundException(id);
        }

        final Task task = new Task(id, source.getTitle());
        taskMap.put(task.getId(), task);

        return task;
    }

    public void deleteTask(Long id) {
        if (taskDoesNotExist(id)) {
            throw new TaskNotFoundException(id);
        }

        taskMap.remove(id);
    }

    private Long generateId() {
        newId += 1;
        return newId;
    }

    private boolean taskDoesNotExist(Long id) {
        final Task task = taskMap.get(id);
        return Objects.isNull(task);
    }
}
