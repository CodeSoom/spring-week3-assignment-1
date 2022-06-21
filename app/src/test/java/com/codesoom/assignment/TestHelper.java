package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

abstract class TestHelper {
    public Task dummyTask(String title) {
        Task newTask = new Task();
        newTask.setTitle(title);

        return newTask;
    }
}
