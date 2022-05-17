package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.TaskRequest;
import com.codesoom.assignment.dto.TaskResponse;
import com.codesoom.assignment.exceptions.NotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    public List<TaskResponse> getTasks() {
        List<TaskResponse> taskListResponse = new ArrayList<>();

        for(Task task : tasks) {
            TaskResponse taskResponse = new TaskResponse(task.getId(), task.getTitle());
            taskListResponse.add(taskResponse);
        }

        return taskListResponse;
    }

    public TaskResponse getTask(Long id) {
        Task task = findTask(id);

        TaskResponse taskResponse = new TaskResponse(task.getId(), task.getTitle());

        return taskResponse;
    }

    public Task findTask(Long id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(id));
    }

    public TaskResponse addTask(TaskRequest taskRequest) {
        Task task = new Task(taskRequest.getTitle());
        task.setId(generateId());
        tasks.add(task);

        TaskResponse taskResponse = new TaskResponse(task.getId(), task.getTitle());
        return taskResponse;
    }

    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        Task task = findTask(id);
        task.updateTitle(taskRequest.getTitle());

        TaskResponse taskResponse = new TaskResponse(task.getId(), task.getTitle());

        return taskResponse;
    }

    public void deleteTask(Long id) {
        Task task = findTask(id);
        tasks.remove(task);
    }

    private Long generateId() {
        newId += 1;
        return newId;
    }
}
