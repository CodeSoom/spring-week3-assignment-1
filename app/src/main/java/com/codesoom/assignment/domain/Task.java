package com.codesoom.assignment.domain;

import com.codesoom.assignment.dto.TaskDto;

import java.util.Objects;

public class Task {

    private final Long id;
    private String title;

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Task createNewTask(Long id, Task task) {
        return new Task(id, task.title);
    }

    public boolean hasValidTitle() {
        return this.title != null;
    }

    public Task changeTitle(Task task) {
        this.title = task.title;
        return this;
    }

    public String currentTitle() {
        return this.title;
    }

    public boolean isMyId(long id) {
        return Long.compare(this.id, id) == 0 ? true : false;
    }

    public TaskDto toTaskDto() {
        return new TaskDto(this.id, this.title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id) && title.equals(task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
