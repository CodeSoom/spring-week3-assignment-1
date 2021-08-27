package com.codesoom.assignment.application;

import com.codesoom.assignment.IdGenerator;
import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 할 일 관리를 담당합니다.
 */
@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private final IdGenerator idGenerator;

    public TaskService(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * 모든 할 일을 리턴합니다.
     *
     * @return 모든 할 일
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * 식별자로 할 일을 찾아 리턴합니다.
     *
     * @param id 식별자
     * @return 찾은 할 일
     * @throws TaskNotFoundException 할 일을 찾지 못한 경우
     */
    public Task getTask(Long id) {
        return tasks.stream()
            .filter(task -> task.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * 할 일을 생성합니다.
     *
     * @param source 생성할 할 일
     * @return 생성된 할 일
     */
    public Task createTask(Task source) {
        long id = idGenerator.generate();

        Task task = new Task(id);
        task.setTitle(source.getTitle());

        tasks.add(task);

        return task;
    }

    /**
     * 식별자로 할 일을 찾고, 수정합니다.
     *
     * @param id     식별자
     * @param source 수정할 할 일
     * @return 수정된 할 일
     */
    public Task updateTask(Long id, Task source) {
        Task task = getTask(id);
        task.setTitle(source.getTitle());

        return task;
    }

    /**
     * 식별자로 할 일을 찾고, 삭제합니다.
     *
     * @param id 식별자
     * @return 삭제된 할 일
     */
    public Task deleteTask(Long id) {
        Task task = getTask(id);
        tasks.remove(task);

        return task;
    }
}
