package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.services.TaskReadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 힐 일 조회 요청과 매핑됩니다.
 */
@TaskController
public class TaskReadController {

    private final TaskReadService service;

    public TaskReadController(TaskReadService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> getTasks() {
        return service.getTasks();
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        return service.findTaskById(id);
    }

}
