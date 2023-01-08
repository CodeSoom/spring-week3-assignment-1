package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;

import java.util.Collection;
import java.util.List;

public interface TaskRepository {

    Collection<Task> findAllTasks();

    List<Task> findAllTasksByDeadLine();

    Task findTaskById(Long id);

    Task addTask(Task task);

    Task updateTitle(Task src);

    Task deleteTask(Long id);
}
