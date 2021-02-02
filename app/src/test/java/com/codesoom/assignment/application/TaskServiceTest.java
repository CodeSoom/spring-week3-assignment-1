package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    @Test
    void getEmptyTasks() {
        TaskService taskService = new TaskService();

        List<Task> emptyTaskList = taskService.getTasks();

        assertEquals(0, emptyTaskList.size());
    }
}
