package com.codesoom.assignment.models;

abstract class TaskModel {
    static final Long TASK_ID = 1L;
    static final String TASK_TITLE = "test";

    Task subject() {
        final Task task = new Task();

        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
        return task;
    }
}
