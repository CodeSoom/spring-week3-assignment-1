package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 할 일 요청 처리를 담당합니다.
 */
@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 전체 할 일을 리턴합니다.
     * @return 전체 할 일
     */
    @GetMapping
    public List<Task> list() {
        return taskService.getTasks();
    }

    /**
     * 식별자로 찾은 할 일을 리턴합니다.
     * @param id 식별자
     * @return 찾은 할 일
     */
    @GetMapping("{id}")
    public Task detail(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    /**
     * 할 일을 생성합니다.
     * @param task 생성할 할 일
     * @return 생성된 할 일
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    /**
     * 식별자로 할 일을 찾고, 수정합니다.
     * @param id 식별자
     * @param task 수정할 할 일
     * @return 수정된 할 일
     */
    @PutMapping("{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    /**
     * 식별자로 할 일을 찾고, 수정합니다.
     * @param id 식별자
     * @param task 수정할 할 일
     * @return 수정된 할 일
     */
    @PatchMapping("{id}")
    public Task patch(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    /**
     * 식별자로 할 일을 찾고, 삭제합니다.
     * @param id 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
