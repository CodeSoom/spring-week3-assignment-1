package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.TaskDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> list() {
        return taskService.list()
                .stream()
                .map(TaskDto::fromTask)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto detail(@PathVariable Long id) {
        return TaskDto.fromTask(taskService.detail(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@RequestBody TaskDto taskDto) {
        return TaskDto.fromTask(taskService.create(taskDto.toTask()));
    }
}
