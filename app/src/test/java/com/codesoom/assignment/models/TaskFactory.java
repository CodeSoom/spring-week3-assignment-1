package com.codesoom.assignment.models;

public class TaskFactory {
    public static Task withIdAndTitle(Long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);

        return task;
    }

    public static Task withTitle(String title) {
        Task task = new Task();
        task.setTitle(title);

        return task;
    }
}
