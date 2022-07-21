package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskMapRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class BasicTaskService implements TaskService{
    private final TaskMapRepository taskMapRepository;

    public BasicTaskService(TaskMapRepository taskMapRepository) {
        this.taskMapRepository = taskMapRepository;
    }

    public Collection<Task> getTasks() {
        return new ArrayList<>(taskMapRepository.getAll());
    }

    public Task getTask(Long id) {
        return taskMapRepository.get(id)
                    .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(String title) {
        return taskMapRepository.add(title);
    }

    public Task updateTask(Long id, String title) {
        return getTask(id).changeTitle(title);
    }

    public void deleteTask(Long id) {
        getTask(id);
        taskMapRepository.remove(id);
    }
}
