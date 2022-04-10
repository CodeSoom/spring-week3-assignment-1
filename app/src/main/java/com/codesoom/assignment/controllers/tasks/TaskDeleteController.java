package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.services.TaskDeleteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * 할 일 삭제 요청과 매핑됩니다.
 */
@TaskController
public class TaskDeleteController {

    private final TaskDeleteService service;

    public TaskDeleteController(TaskDeleteService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        service.deleteTaskById(id);
    }

}