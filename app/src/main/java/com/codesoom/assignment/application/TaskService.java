package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.getAll();
    }

    public Task getTask(Long id) {
        return taskRepository.get(id)
                    .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(String title) {
        return taskRepository.add(title);
    }

    public Task updateTask(Long id, String title) {
        return getTask(id).changeTitle(title);
    }

    public Task deleteTask(Long id) {
        getTask(id);
        return taskRepository.remove(id);
    }
}
