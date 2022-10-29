package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.TaskDto;
import com.codesoom.assignment.utils.IdValidator;
import com.codesoom.assignment.utils.TaskDtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
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
    public Collection<TaskDto> collection() {
        return taskService.getTasks();
    }

    @GetMapping("/deadline")
    public List<TaskDto> collectionByDeadLine() {
        return taskService.getTasksByDeadLine();
    }

    @GetMapping("{id}")
    public TaskDto detail(@PathVariable Long id) {
        IdValidator.validate(id);

        return taskService.getTask(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@RequestBody TaskDto dto) {
        TaskDtoValidator.validate(dto);

        return taskService.createTask(dto);
    }

    @RequestMapping(value = "/{id}",method = {RequestMethod.PUT, RequestMethod.PATCH})
    public TaskDto update(@PathVariable Long id, @RequestBody TaskDto dto) {
        IdValidator.validate(id);
        TaskDtoValidator.validate(dto);

        return taskService.updateTitle(id, dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        IdValidator.validate(id);
        taskService.deleteTask(id);
    }
}
