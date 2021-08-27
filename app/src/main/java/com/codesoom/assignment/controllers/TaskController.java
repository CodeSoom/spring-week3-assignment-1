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
     * 새로운 Task의 생성을 요청하는 경우,
     * 새로운 Task를 생성하고,
     * 생성한 Task를 리턴한다.
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
     * 저장되어 있는 Task의 수정을 요청하는 경우,
     * id에 해당하는 Task를 업데이트하고,
     * 업데이트한 Task를 리턴한다.
     *
     * @param id 업데이트할 Task의 id
     * @param task 업데이트할 Task의 데이터
     * @return 업데이트한 Task
     * @throws TaskNotFoundException id에 해당하는 Task가 없는경우
     * @see TaskErrorAdvice#handleNotFound() TaskNotFoundException 예외처리 수행
     */
    @PutMapping("{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    /**
     * 저장되어 있는 Task의 수정을 요청하는 경우,
     * id에 해당하는 Task를 업데이트하고,
     * 업데이트한 Task를 리턴한다.
     *
     * @param id 업데이트할 Task의 id
     * @param task 업데이트할 Task의 데이터
     * @return 업데이트한 Task
     * @throws TaskNotFoundException id에 해당하는 Task가 없는경우
     * @see TaskErrorAdvice#handleNotFound() TaskNotFoundException 예외처리 수행
     */
    @PatchMapping("{id}")
    public Task patch(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    /**
     * 저장되어 있는 Task의 삭제를 요청하는 경우,
     * id에 해당하는 Task를 삭제한다.
     *
     * @param id 삭제할 Task의 id
     * @throws TaskNotFoundException id에 해당하는 Task가 없는경우
     * @see TaskErrorAdvice#handleNotFound() TaskNotFoundException 예외처리 수행
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
