package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.TaskRepository;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getTaskList() {
        return repository.getTaskList();
    }

    public Task getTask(Long id) {
        return repository.getTaskById(id);
    }

    public Task createTask(Task source) {
        Task task = new Task(source.getTitle());

        repository.addTask(task);

        return task;
    }

    public Task updateTask(Long id, Task source) {
        repository.updateTask(id, source);

        return getTask(id);
    }

    public void deleteTask(Long id) {
        repository.deleteTask(id);
    }
}
