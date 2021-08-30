package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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
     * 할 일을 조회하는 쿼리를 리턴합니다.
     * @return 할 일 조회 쿼리
     */
    public TaskServiceQueries read() {
        return new TaskServiceQueries(tasks);
    }

    /**
     * 새로운 할 일을 저장하고 리턴합니다.
     * @param source 새로운 할 일
     * @return 새로 등록된 할 일
     */
    public Task create(Task source) {
        Long id = (long) source.hashCode();

        tasks.put(id, new Task(id, source.getTitle()));

        return new TaskServiceQueries(tasks).details(id);
    }

    /**
     * id에 해당되는 할 일을 수정하고 리턴합니다.
     * @param id 할 일의 식별자
     * @param source 수정된 할 일
     * @return 수정 된 할 일
     */
    public Task update(Long id, Task source) {
        tasks.replace(id, new Task(id, source.getTitle()));

        return new TaskServiceQueries(tasks).details(id);
    }

    /**
     * id에 해당되는 할 일을 삭제하고 리턴합니다.
     * @param id 할 일의 식별자
     * @return 삭제 된 할 일
     */
    public Task delete(Long id) {
        Task deletedTask = new TaskServiceQueries(tasks).details(id);

        tasks.remove(id);

        return deletedTask;
    }
}
