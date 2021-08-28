package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Task 데이터 처리를 수행한다.
 */
@Service
public class TaskService {
    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    /**
     * 저장되어있는 모든 Task를 리턴한다.
     *
     * @return 저장되어 있는 모든 Task
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * id에 해당하는 Task를 리턴한다.
     *
     * @param id Task의 id
     * @return id에 해당하는 Task
     * @throws TaskNotFoundException id에 해당하는 Task를 찾지 못한 경우
     */
    public Task getTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * 새로운 Task를 생성하고 리턴한다.
     *
     * @param source 새로 생성할 Task 데이터
     * @return 새로 생성한 Task
     */
    public Task createTask(Task source) {
        Task task = new Task(generateId(), source.getTitle());

        tasks.add(task);

        return task;
    }

    /**
     * Task를 업데이트하고 리턴한다.
     *
     * @param id 업데이트할 Task의 id
     * @param source 업데이트할 Task 데이터
     * @return 업데이트한 Task
     * @throws TaskNotFoundException id에 해당하는 Task를 찾지 못한 경우
     */
    public Task updateTask(Long id, Task source) {
        Task task = getTask(id);
        task.setTitle(source.getTitle());

        return task;
    }

    /**
     * Task를 삭제하고 리턴한다.
     *
     * @param id 삭제할 Task의 id
     * @return 삭제한 Task
     * @throws TaskNotFoundException id에 해당하는 Task를 찾지 못한 경우
     */
    public Task deleteTask(Long id) {
        Task task = getTask(id);
        tasks.remove(task);

        return task;
    }

    /**
     * 중복되지 않은 Task id를 생성하고 리턴한다.
     *
     * @return 중복되지 않은 Task id
     */
    private Long generateId() {
        newId += 1;
        return newId;
    }
}
