package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;

import java.util.List;

public interface TaskService {

    List<Task> getTasks();

    Task getTask(Long id);

    Task createTask(Task task);

    Task updateTask(Long id, Task task);

    void deleteTask(Long id);

}
