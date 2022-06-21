package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

abstract class TestHelper {
    public Task dummyTask(String title) {
        Task newTask = new Task();
        newTask.setTitle(title);

        return newTask;
    }

    public Task dummyTask(Long id, String title) {
        Task newTask = new Task();
        newTask.setId(id);
        newTask.setTitle(title);

        return newTask;
    }
}
