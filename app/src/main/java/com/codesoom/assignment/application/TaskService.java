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
     * 사용자의 할 일을 가져오기
     * @param id 가져올 할 일의 id
     * @return 할 일
     */
    public Task getTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * 할일을 할일 목록에 추가
     * @param source 사용자가 입력한 할 일
     * @return 추가된 할 일
     */
    public Task createTask(Task source) {
        Task task = new Task();
        task.setId(generateId());
        task.setTitle(source.getTitle());

        tasks.add(task);

        return task;
    }

    /**
     * 사용자가 선택한 할일 의 내용 수정
     * @param id 사용자가 선택한 할일의 id
     * @param source 사용자가 수정한 내용
     * @return
     */
    public Task updateTask(Long id, Task source) {
        Task task = getTask(id);
        task.setTitle(source.getTitle());

        return task;
    }

    /**
     * 완료된 할 일을 리스트에서 삭제
     * @param id
     * @return 삭제된 결과 반환
     */
    public Task deleteTask(Long id) {
        Task task = getTask(id);
        tasks.remove(task);

        return task;
    }

    /**
     * 할일이 추가될때마다 id를 하나 증가
     * @return 증가된 할 일
     */
    private Long generateId() {
        newId += 1;
        return newId;
    }
}
