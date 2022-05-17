package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.TaskRequest;
import com.codesoom.assignment.dto.TaskResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping("{id}")
    public TaskResponse getTask(@PathVariable("id") Long id) {
        return taskService.getTask(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TaskResponse create(@RequestBody TaskRequest taskRequest) {
        return taskService.addTask(taskRequest);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable("id") Long id, @RequestBody TaskRequest taskRequest) {
        return taskService.updateTask(id, taskRequest);
    }

    @PatchMapping("/{id}")
    public TaskResponse patch(@PathVariable("id") Long id, @RequestBody TaskRequest taskRequest) {
        return taskService.updateTask(id, taskRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = {"", "/", "/{id}"})
    public void delete(@PathVariable(required = false) Long id) {
        taskService.deleteTask(id);
    }
}
