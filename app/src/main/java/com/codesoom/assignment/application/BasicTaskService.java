package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class BasicTaskService implements TaskService{
    private final TaskRepository taskRepository;

    public BasicTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Collection<Task> getTasks() {
        return new ArrayList<>(taskRepository.getAll());
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

    public void deleteTask(Long id) {
        getTask(id);
        taskRepository.remove(id);
    }
}
