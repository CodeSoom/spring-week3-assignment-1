package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private List<Task> tasks = new ArrayList<>(); // 할일 목록
    private Long newId = 0L;

    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * id에 해당하는 할 일을 리턴합니다.
     * @param id 조회할 할 일의 id
     * @return 할 일
     */
    public Task getTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * 주어진 할 일을 등록하고, 리턴합니다.
     * @param source 등록할 할 일
     * @return 할 일
     */
    public Task createTask(Task source) {
        Task task = new Task();
        task.setId(generateId());
        task.setTitle(source.getTitle());

        tasks.add(task);

        return task;
    }

    /**
     * 할 일을 수정하고, 수정된 할 일을 리턴합니다.
     * @param id 수정할 할 일의 id
     * @param source 수정할 내용
     * @return 수정이 완료된 할 일
     */
    public Task updateTask(Long id, Task source) {
        Task task = getTask(id);
        task.setTitle(source.getTitle());

        return task;
    }

    /**
     * 할 일을 삭제하고, 삭제된 할 일을 리턴합니다.
     * @param id 삭제ㄷ할 할 일의 아이디
     * @return 삭제된 할 일
     */
    public Task deleteTask(Long id) {
        Task task = getTask(id);
        tasks.remove(task);

        return task;
    }

    /**
     * 아이디 값을 증가시킵니다.
     * @return 증가된 아이디 값
     */
    private Long generateId() {
        newId += 1;
        return newId;
    }
}
