package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

/**
 * 할 일을 반환하고 생성,수정,삭제하는 일을 처리합니다.
 */

@Service
public class TaskService {
    private final HashMap<Long, Task> tasks;

    public TaskService(HashMap<Long, Task> tasks) {
        this.tasks = tasks;
    }

    public TaskService() {
        this(new HashMap<>());
    }

    /**
     * 할 일 목록을 리턴합니다.
     * @return 할 일 목록
     */
    public Collection<Task> getTasks() {
        return tasks.values();
    }

    /**
     * id에 해당되는 할 일을 리턴합니다.
     * @param id 할 일의 식별자
     * @return 할 일
     * @throws TaskNotFoundException 해당하는 식별자의 할 일을 찾지 못한 경우
     */
    public Task getTask(Long id) {
        return Optional.ofNullable(tasks.get(id))
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * 새로운 할 일을 저장하고 리턴합니다.
     * @param source 새로운 할 일
     * @return 새로 등록된 할 일
     */
    public Task createTask(Task source) {
        Long id = (long) source.hashCode();
        Task newTask = new Task(id, source.getTitle());

        tasks.put(id, newTask);

        return newTask;
    }

    /**
     * id에 해당되는 할 일을 수정하고 리턴합니다.
     * @param id 할 일의 식별자
     * @param source 수정된 할 일
     * @return 수정 된 할 일
     * @throws TaskNotFoundException 해당하는 식별자의 할 일을 찾지 못한 경우
     */
    public Task updateTask(Long id, Task source) {
        Task task = tasks.replace(id, new Task(id, source.getTitle()));

        return Optional.ofNullable(task)
                .map((oldTask) -> new Task(id, source.getTitle()))
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * id에 해당되는 할 일을 삭제하고 리턴합니다.
     * @param id 할 일의 식별자
     * @return 삭제 된 할 일
     * @throws TaskNotFoundException 해당하는 식별자의 할 일을 찾지 못한 경우
     */
    public Task deleteTask(Long id) {
        Task task = tasks.remove(id);

        return Optional.ofNullable(task)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
