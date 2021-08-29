package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskCommandService {

    private final TaskRepository taskRepository;

    private final TaskQueryService taskQueryService;

    public TaskCommandService(TaskRepository taskRepository, TaskQueryService taskQueryService) {
        this.taskRepository = taskRepository;
        this.taskQueryService = taskQueryService;
    }

    /**
     * 새로운 할 일을 생성합니다.
     *
     * @param task 생성 요청된 할 일
     * @return Task 생성된 할 일
     */
    public Task createTask(Task task) {
        taskRepository.createNewTaskId();
        Task newTask = new Task(taskRepository.getNewId(), task.getTitle());
        taskRepository.addTask(newTask);

        return newTask;
    }

    /**
     * 변경 요청된 할 일을 찾아서 title을 변경합니다.
     *
     * @param id 수정할 식별자 Id
     * @param requestTask 변경 요청된 할 일
     * @return Task 변경된 할 일
     */
    public Task updateTask(Long id, Task requestTask) {
        Task task = taskQueryService.getTask(id);

        Task updateTask = new Task(task.getId(), requestTask.getTitle());
        taskRepository.removeTask(task);
        taskRepository.addTask(updateTask);
        return updateTask;
    }

    /**
     * 완료된 할 일을 삭제합니다.
     *
     * @param id 완료된 Id 식별자
     */
    public void completeTask(Long id) {
        taskRepository.removeTask(taskQueryService.getTask(id));
    }
}
