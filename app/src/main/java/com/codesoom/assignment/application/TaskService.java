package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 할 일을 반환하고 생성,수정,삭제하는 일을 처리합니다.
 */

@Service
public class TaskService {
    private List<Task> tasks;
    private Long newId;

    public TaskService(Long initialId, List<Task> tasks) {
        this.newId = initialId;
        this.tasks = tasks;
    }

    public TaskService() {
        this(0L, new ArrayList<>());
    }

    /**
     * 할 일 목록을 리턴합니다.
     * @return 할 일 목록
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * id에 해당되는 할 일을 리턴합니다.
     * @param id 할 일의 식별자
     * @return 할 일
     */
    public Task getTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * 새로운 할 일을 저장하고 리턴합니다.
     * @param source 새로운 할 일
     * @return 새로 등록된 할 일
     */
    public Task createTask(Task source) {
        Task task = new Task(generateId(), source.getTitle());

        tasks.add(task);

        return task;
    }

    /**
     * id에 해당되는 할 일을 수정하고 리턴합니다.
     * @param id 할 일의 식별자
     * @param source 수정된 할 일
     * @return 수정 된 할 일
     */
    public Task updateTask(Long id, Task source) {
        Task task = getTask(id);

        return new Task(task.getId(), source.getTitle());
    }

    /**
     * id에 해당되는 할 일을 삭제하고 리턴합니다.
     * @param id 할 일의 식별자
     * @return 삭제 된 할 일
     */
    public Task deleteTask(Long id) {
        Task task = getTask(id);
        tasks.remove(task);

        return task;
    }

    /**
     * 새로운 식별자를 생성하여 리턴합니다.
     * @return 새로운 식별자
     */
    private Long generateId() {
        newId += 1;
        return newId;
    }
}
