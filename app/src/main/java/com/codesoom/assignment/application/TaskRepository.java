package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.IdGenerator;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 할 일을 저장하고, 조회, 수정, 삭제 기능을 제공하는 클래스입니다.
 */
@Repository
public class TaskRepository {
    private final IdGenerator taskIdGenerator;
    private Map<Long, Task> taskMap = new ConcurrentHashMap<>();

    public TaskRepository(IdGenerator taskIdGenerator) {
        this.taskIdGenerator = taskIdGenerator;
    }

    /**
     * 할 일 목록 전체를 반환합니다.
     * @return 할 일 목록
     */
    public Collection<Task> getTaskList() {
        return taskMap.values();
    }

    /**
     * 주어진 id를 가진 할 일을 반환합니다.
     * @param id 할 일 식별자
     * @return 할 일
     */
    public Optional<Task> getTaskById(Long id) {
        return Optional.ofNullable(taskMap.get(id));
    }

    /**
     * 주어진 할 일을 추가합니다.
     * @param task 할 일
     * @return 추가된 할 일
     */
    public Task addTask(Task task) {
        Task newTask = new Task(taskIdGenerator.nextSequence());
        newTask.setTitle(task.getTitle());
        taskMap.put(newTask.getId(), newTask);
        return newTask;
    }

    /**
     * 식별자와, 할 일을 받아서 기존의 할 일을 대체합니다.
     * @param id 할 일 식별자
     * @param task 교체할 새로운 할 일
     * @return 교체한 할 일
     */
    public Optional<Task> replaceTask(Long id, Task task) {
        Optional<Task> oldTask = Optional.ofNullable(taskMap.get(id));
        oldTask.ifPresent(it -> {
            it.setTitle(task.getTitle());
            taskMap.put(it.getId(), it);
        });
        return oldTask;
    }

    /**
     * 식별자와, 할 일을 받아서 기존의 할 일을 수정합니다.
     * @param id 할 일 식별자
     * @param task 수정할 새로운 할 일
     * @return 수정한 할 일
     */
    public Optional<Task> updateTask(Long id, Task task) {
        Optional<Task> oldTask = Optional.ofNullable(taskMap.get(id));
        oldTask.ifPresent(it -> {
            it.setTitle(task.getTitle());
            taskMap.put(it.getId(), it);
        });
        return oldTask;
    }

    /**
     * 식별자로 할 일을 찾아서, 삭제합니다.
     * @param id 할 일 식별자
     * @return 삭제된 할 일
     */
    public Optional<Task> deleteTask(Long id) {
        Task task = taskMap.get(id);
        taskMap.remove(id);
        return Optional.ofNullable(task);
    }
}
