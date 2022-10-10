package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.TaskRequestDto;
import com.codesoom.assignment.models.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> list() {
        return taskService.getTasks();
    }

    @GetMapping("{id}")
    public Task detail(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody TaskRequestDto task) {
        return taskService.createTask(task);
    }

    @RequestMapping(path = "{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public Task update(@PathVariable Long id, @RequestBody TaskRequestDto task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
