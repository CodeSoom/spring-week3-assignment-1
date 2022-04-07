package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.TaskEditDto;
import com.codesoom.assignment.dto.TaskSaveDto;
import com.codesoom.assignment.dto.TaskViewDto;
import com.codesoom.assignment.models.Task;
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
    public List<TaskViewDto> list() {

        List<Task> tasks = taskService.getTasks();

        return tasks.stream()
                .map(TaskViewDto::from)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public TaskViewDto detail(@PathVariable Long id) {

        final Task task = taskService.getTask(id);

        return TaskViewDto.from(task);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskViewDto create(@RequestBody TaskSaveDto source) {

        final Task task = taskService.createTask(source);

        return TaskViewDto.from(task);
    }

    @PutMapping("{id}")
    public TaskViewDto update(@PathVariable Long id, @RequestBody TaskEditDto source) {

        final Task task = taskService.updateTask(id, source);

        return TaskViewDto.from(task);
    }

    @PatchMapping("{id}")
    public TaskViewDto patch(@PathVariable Long id, @RequestBody TaskEditDto source) {

        final Task task = taskService.updateTask(id, source);

        return TaskViewDto.from(task);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
