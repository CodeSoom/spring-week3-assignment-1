package com.codesoom.assignment;

import com.codesoom.assignment.TaskRepository;

public class TaskRepositoryCleaner {
    final private TaskRepository repository;

    public TaskRepositoryCleaner(TaskRepository repository) {
        this.repository = repository;
    }

    public void deleteAllTasks() {
        repository.getTaskList()
                .forEach(task -> {
                    repository.deleteTask(task.getId());
                });
    }
}
