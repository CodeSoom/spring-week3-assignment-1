package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;

import java.util.Collection;

public interface TaskRepository {

    Collection<Task> findAllTasks();

    Task findTaskById(Long id);

    Task addTask(Task task);

    Task updateTask(Long id, Task source);

    Task deleteTask(Long id);
}
