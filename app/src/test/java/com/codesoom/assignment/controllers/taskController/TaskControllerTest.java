package com.codesoom.assignment.controllers.taskController;

import java.util.ArrayList;
import java.util.List;

import com.codesoom.assignment.models.Task;

public abstract class TaskControllerTest {
    protected final Long validId = 1L;
    protected final Long invalidId = 2L;
    protected final String taskTitle = "title";
    protected final Task task = new Task(validId, taskTitle);
    protected final List<Task> tasks = new ArrayList<>();
}