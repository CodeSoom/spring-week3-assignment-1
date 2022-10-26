package com.codesoom.assignment.controllers;

import com.codesoom.assignment.exceptions.NegativeIdException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public Collection<Task> collection() {
        return taskService.getTasks();
    }

    @GetMapping("{id}")
    public Task detail(@PathVariable Long id) {
        if (id < 0) {
            throw new NegativeIdException(id);
        }

        return taskService.getTask(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @PatchMapping("{id}")
    public Task patch(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
