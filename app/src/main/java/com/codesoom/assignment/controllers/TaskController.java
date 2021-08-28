package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * 할 일에 대한 HTTP 요청의 처리를 담당합니다.
 */
@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 할 일 목록을 리턴합니다.
     * @return 할 일 목록
     */
    @GetMapping
    public Collection<Task> list() {
        return taskService.read().all();
    }

    /**
     * 사용자가 요청한 id의 할 일을 리턴합니다.
     * @param id 요청한 식별자
     * @return 할 일
     */
    @GetMapping("{id}")
    public Task detail(@PathVariable Long id) {
        return taskService.read().details(id);
    }

    /**
     * 사용자가 요청한 할 일을 생성합니다.
     * @param task 요청한 할 일
     * @return 생성된 할 일
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task task) {
        return taskService.create(task);
    }

    /**
     * 사용자가 요청한 id의 할 일을 수정합니다.
     * @param id 요청한 식별자
     * @param task 요청한 할 일
     * @return 수정된 할 일
     */
    @PutMapping("{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return taskService.update(id, task);
    }

    /**
     * 사용자가 요청한 id의 할 일을 수정합니다.
     * @param id 요청한 식별자
     * @param task 요청한 할 일
     * @return 수정된 할 일
     */
    @PatchMapping("{id}")
    public Task patch(@PathVariable Long id, @RequestBody Task task) {
        return taskService.update(id, task);
    }

    /**
     * 사용자가 요청한 id의 할 일을 삭제합니다.
     * @param id 요청한 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Task delete(@PathVariable Long id) {
        return taskService.delete(id);
    }
}
