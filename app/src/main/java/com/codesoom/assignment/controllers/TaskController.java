package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Task에 대한 HTTP 요청 처리를 담당한다.
 */
@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskRepository) {
        this.taskService = taskRepository;
    }

    /**
     * Task 목록을 반환한다
     * @return Task 목록
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Task> getTaskList() {
        return taskService.getTaskList();
    }

    /**
     * 사용자가 요청한 id와 동일한 id를 가진 Task를 반환한다
     * @param id 요청한 Task 식별자
     * @return Task 객체
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * 사용자가 요청한 Task를 추가한다.
     * @param task 요청한 추가 대상
     * @return 추가된 Task
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task addTask(@RequestBody Task task) {
        return taskService.addTask(task);
    }

    /**
     * 사용자가 요청한 Task를 교체한다.
     * @param id 요청한 Task 식별자
     * @param task 교체할 Task
     * @return 교체한 Task
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task replaceTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.replaceTask(id, task).orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * 사용자가 요청한 Task를 수정한다
     * @param id 요청한 Task 식별자
     * @param task 수정할 Task
     * @return 수정한 Task
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task).orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * 사용자가 요청한 Task를 삭제한다
     * @param id Task 식별자
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id).orElseThrow(() -> new TaskNotFoundException(id));
    }
}
