package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private List<Task> tasks;
    private Long maxId = 0L;

    public TaskService() {
        this.tasks = new ArrayList<>();
    }

    public List<Task> list() {
        return tasks;
    }

    public Task create(Task task) {
        Task created = Task.createNewTask(generateId(), task);
        tasks.add(created);
        return created;
    }

    private Long generateId() {
        maxId += 1;
        return maxId;
    }
}
