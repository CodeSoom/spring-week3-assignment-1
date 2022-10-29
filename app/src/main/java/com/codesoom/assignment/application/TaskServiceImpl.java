package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskDto;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.utils.TaskIdGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskIdGenerator idGenerator = new TaskIdGenerator();

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Collection<TaskDto> getTasks() {
        return taskRepository.findAllTasks().stream()
                .map(this::taskToDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<TaskDto> getTasksByDeadLine() {
        return taskRepository.findAllTasksByDeadLine().stream()
                .map(this::taskToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto getTask(Long id) {
        final Task task = taskRepository.findTaskById(id);
        return taskToDto(task);
    }

    @Override
    public TaskDto createTask(TaskDto dto) {
        final Task task = dtoToTask(idGenerator.generateId(), dto);
        final Task addedTask = taskRepository.addTask(task);

        return taskToDto(addedTask);
    }

    @Override
    public TaskDto updateTitle(Long id, TaskDto dto) {
        final Task task = dtoToTask(id, dto);
        final Task updatedTask = taskRepository.updateTitle(task);

        return taskToDto(updatedTask);
    }

    @Override
    public TaskDto deleteTask(Long id) {
        final Task deleteTask = taskRepository.deleteTask(id);
        return taskToDto(deleteTask);
    }

}
