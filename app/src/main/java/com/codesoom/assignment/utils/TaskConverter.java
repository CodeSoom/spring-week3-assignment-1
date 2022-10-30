package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskDto;

import java.time.LocalDateTime;

public class TaskConverter {

    public static TaskDto toDto(Task task) {
        return TaskDto.from(task);
    }

    public static Task toTask(Long id, TaskDto dto) {
        return Task.builder()
                .id(id)
                .title(dto.getTitle())
                .regDate(LocalDateTime.now())
                .modDate(LocalDateTime.now())
                .deadLine(dto.getDeadLine())
                .build();
    }
}
