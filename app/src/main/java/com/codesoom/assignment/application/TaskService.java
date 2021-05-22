package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskEmptyTitleException;
import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(Task source) {
        if (source == null || StringUtils.isEmpty(source.getTitle())) {
            throw new TaskEmptyTitleException();
        }

        Task task = new Task();
        task.setId(generateId());
        task.setTitle(source.getTitle());

        tasks.add(task);

        return task;
    }

    public Task updateTask(Long id, Task source) {
        Task task = getTask(id);
        task.setTitle(source.getTitle());

        return task;
    }

    public Task deleteTask(Long id) {
        Task task = getTask(id);
        tasks.remove(task);

        return task;
    }

    public Long currentId() {
        return newId;
    }

    private Long generateId() {
        newId += 1;
        return newId;
    }
}
