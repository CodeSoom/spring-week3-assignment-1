package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Collection<Task> getTaskList() {
        return taskRepository.getTaskList();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.getTaskById(id);
    }

    public Task addTask(Task task) {
        return taskRepository.addTask(task);
    }

    public Optional<Task> replaceTask(Long id, Task task) {
        return taskRepository.replaceTask(id, task);
    }

    public Optional<Task> updateTask(Long id, Task task) {
        return taskRepository.updateTask(id, task);
    }

    public Optional<Task> deleteTask(Long id) {
        return taskRepository.deleteTask(id);
    }
}
