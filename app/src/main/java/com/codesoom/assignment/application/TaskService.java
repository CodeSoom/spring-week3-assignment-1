package com.codesoom.assignment.application;

import com.codesoom.assignment.models.TaskDto;

import java.util.Collection;
import java.util.List;

public interface TaskService {

    Collection<TaskDto> getTasks();

    List<TaskDto> getTasksByDeadLine();

    TaskDto getTask(Long id);

    TaskDto createTask(TaskDto dto);

    TaskDto updateTitle(Long id, TaskDto dto);

    TaskDto deleteTask(Long id);
}
