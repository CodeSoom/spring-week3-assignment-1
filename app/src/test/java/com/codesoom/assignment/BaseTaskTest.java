package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

public class BaseTaskTest {

    protected static final long TASK_ID_1 = 1L;
    protected static final String TASK_TITLE_1 = "To complete CodeSoom assignment";
    protected static final String TASK_TITLE_2 = "To write test code";
    protected static final String ERROR_MSG_TASK_NOT_FOUND = "Task not found";

    protected Task generateNewTask(String taskTitle) {
        Task newTask = new Task();
        newTask.setTitle(taskTitle);

        return newTask;
    }

}
