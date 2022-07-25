package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 할 일을 추가, 삭제, 수정, 조회 합니다
 * 도메인 Layer와 데이터 접근 Layer 사이의 Mediator 역할을 합니다
 * 현재는 in-memory 방식으로 데이터를 관리합니다
 */
@Repository
public class TaskRepository {
    private final List<Task> tasks = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong newId = new AtomicLong(0L);

    /**
     * 할 일 Id로 할 일을 조회합니다
     * @param taskId 할 일 Id
     * @return 조회된 할 일
     */
    public Task getTaskById(Long taskId) {
        return getOptionalTask(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    /**
     * 할 일을 저장소에 추가합니다
     * @param task 저장소에 추가할 할 일
     */
    public void addTask(Task task) {
        task.setId(generateId());
        tasks.add(task);
    }

    public void updateTask(Long taskId, Task task) {
        Task oldTask = getTaskById(taskId);
        oldTask.setTitle(task.getTitle());
    }

    /**
     * 할 일을 저장소에서 제거합니다
     * @param taskId 저장소에서 제거할 할 일 id
     */
    public void deleteTask(Long taskId) {
        if (getOptionalTask(taskId).isEmpty()) {
            throw new TaskNotFoundException(taskId);
        }

        tasks.removeIf(task -> {
            return task.getId().equals(taskId);
        });
    }

    /**
     * 저장된 할 일 목록을 가져옵니다
     * @return 할 일 목록
     */
    public List<Task> getTaskList() {
        return tasks;
    }

    private Long generateId() {
        return newId.incrementAndGet();
    }

    private Optional<Task> getOptionalTask(Long taskId) {
        return tasks
                .stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst();
    }
}
