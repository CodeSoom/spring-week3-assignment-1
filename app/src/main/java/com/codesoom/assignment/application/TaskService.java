package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface TaskService {

    Collection<TaskDto> getTasks();

    List<TaskDto> getTasksByDeadLine();

    TaskDto getTask(Long id);

    TaskDto createTask(TaskDto dto);

    TaskDto updateTitle(Long id, TaskDto dto);

    TaskDto deleteTask(Long id);

    default TaskDto taskToDto(Task task) {
        return TaskDto.from(task);
    }

    default Task dtoToTask(Long id, TaskDto dto) {
        return Task.builder()
                .id(id)
                .title(dto.getTitle())
                .regDate(LocalDateTime.now())
                .modDate(LocalDateTime.now())
                .deadLine(dto.getDeadLine())
                .build();
    }
}
