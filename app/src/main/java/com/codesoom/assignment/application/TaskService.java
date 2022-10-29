package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;

import java.util.Collection;

public interface TaskService {

    Collection<Task> getTasks();

    Task getTask(Long id);

    Task createTask(Task source);

    Task updateTask(Long id, Task source);

    Task deleteTask(Long id);
}
