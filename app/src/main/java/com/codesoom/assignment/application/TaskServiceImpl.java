package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.utils.TaskIdGenerator;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskIdGenerator idGenerator = new TaskIdGenerator();

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Collection<Task> getTasks() {
        return taskRepository.findAllTasks();
    }

    public Task getTask(Long id) {
        return taskRepository.findTaskById(id);
    }

    public Task createTask(Task source) {
        return taskRepository.addTask(new Task(idGenerator.generateId(), source.getTitle()));
    }

    public Task updateTask(Long id, Task source) {
        return taskRepository.updateTask(id, source);
    }

    public Task deleteTask(Long id) {
        return taskRepository.deleteTask(id);
    }

}
