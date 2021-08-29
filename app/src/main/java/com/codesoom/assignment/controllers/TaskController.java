package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Task와 관련된 클라이언트의 요청을 처리한다.
 */
@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private TaskService taskService;

    /**
     * @param taskService Task 데이터 처리 목적
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 등록된 모든 Task의 목록을 리턴합니다.
     *
     * @return 모든 Task 목록
     */
    @GetMapping
    public List<Task> list() {
        return taskService.getTasks();
    }

    /**
     * id에 해당하는 Task를 리턴한다.
     *
     * @param id Task의 id
     * @return Task
     * @throws TaskNotFoundException id에 해당하는 Task를 찾지 못한 경우
     */
    @GetMapping("{id}")
    public Task detail(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    /**
     * 새로운 Task를 생성해 리턴합니다.
     *
     * @param task 새로 생성할 Task 내용
     * @return 새로 생성한 Task
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    /**
     * id에 해당하는 Task를 업데이트하고 리턴한다.
     *
     * @param id 업데이트할 Task의 id
     * @param task 업데이트할 Task의 데이터
     * @return 업데이트한 Task
     * @throws TaskNotFoundException id에 해당하는 Task를 찾지 못한 경우
     * @see TaskErrorAdvice#handleNotFound()
     */
    @RequestMapping(
        value = "{id}", method = { RequestMethod.PUT, RequestMethod.PATCH }
        )
    @ResponseStatus(HttpStatus.OK)
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    /**
     * id에 해당하는 Task를 삭제한다.
     *
     * @param id 삭제할 Task의 id
     * @throws TaskNotFoundException id에 해당하는 Task를 찾지 못한 경우
     * @see TaskErrorAdvice#handleNotFound()
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
