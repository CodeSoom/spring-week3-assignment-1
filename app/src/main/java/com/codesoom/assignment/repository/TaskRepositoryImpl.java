package com.codesoom.assignment.repository;

import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final Map<Long, Task> tasks = Collections.synchronizedMap(new HashMap<>());
    private final Set<String> titles = Collections.synchronizedSet(new HashSet<>());
    private final Set<Task> tasksByDeadLine = Collections.synchronizedSet(new TreeSet<>(Comparator.comparing(Task::getDeadLine)));

    @Override
    public Collection<Task> findAllTasks() {
        return Collections.unmodifiableCollection(tasks.values());
    }

    @Override
    public List<Task> findAllTasksByDeadLine() {
        return List.copyOf(tasksByDeadLine);
    }

    @Override
    public Task findTaskById(Long id) {
        final Task task = tasks.get(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }

        return task;
    }

    @Override
    public Task addTask(Task task) {
        final String title = task.getTitle();
        if (titles.contains(title)) {
            throw new DuplicateTaskException(title);
        }
        addToAllStorage(task);
        return task;
    }

    @Override
    public Task updateTitle(Task src) {
        if (titles.contains(src.getTitle())) {
            throw new DuplicateTaskException(src.getTitle());
        }

        final Task originalTask = tasks.get(src.getId());
        if (originalTask == null) {
            throw new TaskNotFoundException(src.getId());
        }

        final Task changedTask = originalTask.changeTitle(src);
        removeFromAllStorage(originalTask.getId());
        addToAllStorage(changedTask);

        return changedTask;
    }

    @Override
    public Task deleteTask(Long id) {
        final Task task = tasks.get(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }

        removeFromAllStorage(id);
        return task;
    }

    private void addToAllStorage(Task task) {
        tasks.put(task.getId(), task);
        titles.add(task.getTitle());
        tasksByDeadLine.add(task);
    }

    private void removeFromAllStorage(Long id) {
        final Task removeTask = tasks.remove(id);
        titles.remove(removeTask.getTitle());
        tasksByDeadLine.remove(removeTask);
    }
}
