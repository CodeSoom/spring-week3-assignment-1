package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {

//    private final TaskService taskService = new TaskService();
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Task> list() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task detail(@PathVariable Long id) {
        System.out.println("id = " + id);
        return taskService.getTask(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task task) {
        System.out.println("task = " + task);
        return taskService.createTask(task);
    }

    @RequestMapping(value = "{id}",method = {RequestMethod.PATCH,RequestMethod.PUT})
    public Task update(
            @PathVariable Long id,
            @RequestBody Task task
    ) {
        System.out.println("id = " + id);
        return taskService.updateTask(id, task);
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
