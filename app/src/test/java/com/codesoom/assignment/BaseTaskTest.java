package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

public abstract class BaseTaskTest {

    protected static final String TASK_TITLE_1 = "sample title";

    protected static final String TASK_TITLE_2 = "sample title 2";
    protected static final Long TASK_ID = 1L;
    protected static final String ERROR_MSG = "Task not found";

    protected Task supplyDummyTask(Long id, String title) {
        Task task = new Task();
        task.setTitle(title);
        task.setId(id);

        return task;
    }

    protected Task supplyDummyTask(String title) {
        return supplyDummyTask(null, title);
    }

    protected String supplyErrorMSG(Long id){
        return ERROR_MSG + ": " + id;
    }
}
