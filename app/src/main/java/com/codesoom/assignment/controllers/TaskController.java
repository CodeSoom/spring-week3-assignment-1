package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.BasicTaskService;
import com.codesoom.assignment.models.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private BasicTaskService basicTaskService;

    public TaskController(BasicTaskService basicTaskService) {
        this.basicTaskService = basicTaskService;
    }

    @GetMapping
    public Collection<Task> list() {
        return basicTaskService.getTasks();
    }

    @GetMapping("{id}")
    public Task detail(@PathVariable Long id) {
        return basicTaskService.getTask(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task task) {
        return basicTaskService.createTask(task.getTitle());
    }

    @PutMapping("{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return basicTaskService.updateTask(id, task.getTitle());
    }

    @PatchMapping("{id}")
    public Task patch(@PathVariable Long id, @RequestBody Task task) {
        return basicTaskService.updateTask(id, task.getTitle());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        basicTaskService.deleteTask(id);
    }
}
