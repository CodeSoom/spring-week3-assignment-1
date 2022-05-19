package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
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
        return taskService.list().stream().map(Task::toTaskDto).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto detail(@PathVariable Long id) {
        return taskService.detail(id).toTaskDto();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@RequestBody TaskDto taskDto) {
        return taskService.create(taskDto.toTask()).toTaskDto();
    }
}
