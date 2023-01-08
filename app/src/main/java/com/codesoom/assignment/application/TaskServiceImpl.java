package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskDto;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.utils.TaskConverter;
import com.codesoom.assignment.utils.TaskIdGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskIdGenerator idGenerator;

    public TaskServiceImpl(TaskRepository taskRepository, TaskIdGenerator idGenerator) {
        this.taskRepository = taskRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Collection<TaskDto> getTasks() {
        return taskRepository.findAllTasks().stream()
                .map(TaskConverter::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<TaskDto> getTasksByDeadLine() {
        return taskRepository.findAllTasksByDeadLine().stream()
                .map(TaskConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto getTask(Long id) {
        final Task task = taskRepository.findTaskById(id);
        return TaskConverter.toDto(task);
    }

    @Override
    public TaskDto createTask(TaskDto dto) {
        final Task task = TaskConverter.toTask(idGenerator.generateId(), dto);
        final Task addedTask = taskRepository.addTask(task);

        return TaskConverter.toDto(addedTask);
    }

    @Override
    public TaskDto updateTitle(Long id, TaskDto dto) {
        final Task task = TaskConverter.toTask(id, dto);
        final Task updatedTask = taskRepository.updateTitle(task);

        return TaskConverter.toDto(updatedTask);
    }

    @Override
    public TaskDto deleteTask(Long id) {
        final Task deletedTask = taskRepository.deleteTask(id);
        return TaskConverter.toDto(deletedTask);
    }

}
