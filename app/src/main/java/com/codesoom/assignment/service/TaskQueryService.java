package com.codesoom.assignment.service;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskQueryService {

    private final TaskRepository taskRepository;

    public TaskQueryService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * 할 일 리스트를 전체 조회합니다.
     *
     * @return List<Task> 전체 할 일 리스트
     */
    public List<Task> getTaskList() {
        return taskRepository.getTasks();
    }

    /**
     * id에 해당하는 할 일을 조회합니다.
     *
     * @param id 조회할 식별자 Id
     * @return Task 조회한 할 일
     */
    public Task getTask(Long id) {
        return taskRepository.findTaskById(id)
                .orElseThrow(() -> new TaskNotFoundException(Long.toString(id)));
    }

}
