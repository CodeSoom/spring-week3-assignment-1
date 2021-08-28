package com.codesoom.assignment.controllers;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.models.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 할 일에 대한 생성, 조회, 수정, 삭제 요청을 처리합니다.
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
     * 할 일 리스트를 리턴합니다.
     * @return 전체 할 일 리스트
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Task> getTaskList() {
        return taskService.getTaskList();
    }

    /**
     * id에 해당하는 할 일을 리턴합니다.
     *
     * @param id 조회할 식별자 Id
     * @return 조회한 할 일
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    /**
     * 새로운 할 일을 등록한 후 등록한 할 일을 리턴합니다.
     *
     * @param task 생성 요청된 할 일
     * @return 생성된 할 일
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    /**
     * 할 일을 찾아서 title을 변경 후 변경된 할 일을 리턴합니다.
     *
     * @param id 수정할 식별자 Id
     * @param requestTask 변경 요청된 할 일
     * @return 변경된 할 일
     */
    @RequestMapping(value = "{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(HttpStatus.OK)
    public Task updateTask(@PathVariable Long id, @RequestBody Task requestTask) {
        return taskService.updateTask(id, requestTask);
    }

    /**
     * 완료된 할 일을 삭제합니다.
     *
     * @param id 완료된 Id 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void completeTask(@PathVariable Long id) {
        taskService.completeTask(id);
    }

}
