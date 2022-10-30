package com.codesoom.assignment.application;

import com.codesoom.assignment.exceptions.TaskDuplicationException;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    public List<Task> getTasks() {

        tasks.sort((o1, o2) -> {
            if (o1.getEndTime().equals(o2.getEndTime())) {
                return o1.getId().compareTo(o2.getId());
            }

            return o1.getEndTime().compareTo(o2.getEndTime());
        });

        return tasks;
    }

    public Task getTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(Task source) {
        if (isExistTask(source)) {
            throw new TaskDuplicationException();
        }

        Task task = Task.builder()
                .id(generateId())
                .title(source.getTitle())
                .endTime(source.getEndTime())
                .build();
        
        tasks.add(task);

        return task;
    }

    public Task updateTask(Long id, Task source) {
        Task originTask = getTask(id);

        Task newTask = Task.builder()
                .id(id)
                .title(source.getTitle())
                .endTime(source.getEndTime())
                .build();

        originTask = newTask;

        return originTask;
    }

    public Task deleteTask(Long id) {
        Task task = getTask(id);
        tasks.remove(task);

        return task;
    }

    private Long generateId() {
        newId += 1;
        return newId;
    }

    private boolean isExistTask(Task source) {
        return tasks.stream()
                .map(Task::getTitle)
                .anyMatch(title -> title.equals(source.getTitle()));
    }
}
