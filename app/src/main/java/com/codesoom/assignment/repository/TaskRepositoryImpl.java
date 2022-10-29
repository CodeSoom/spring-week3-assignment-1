package com.codesoom.assignment.repository;

import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final Map<Long, Task> taskMap = new TreeMap<>();

    @Override
    public Collection<Task> findAllTasks() {
        return Collections.unmodifiableCollection(taskMap.values());
    }

    @Override
    public Task findTaskById(Long id) {
        final Task task = taskMap.get(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }

        return task;
    }

    @Override
    public Task addTask(Task task) {
        taskMap.put(task.getId(), task);
        return task;
    }

    @Override
    public Task updateTask(Long id, Task source) {
        if (!taskMap.containsKey(id)) {
            throw new TaskNotFoundException(id);
        }

        return addTask(new Task(id, source.getTitle()));
    }

    @Override
    public Task deleteTask(Long id) {
        if (!taskMap.containsKey(id)) {
            throw new TaskNotFoundException(id);
        }

        return taskMap.remove(id);
    }
}
