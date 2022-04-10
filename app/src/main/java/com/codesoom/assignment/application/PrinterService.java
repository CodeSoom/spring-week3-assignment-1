package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;

public class PrinterService {

    public static String taskToJson(Task task) {
        return String.format("{\"id\":%s,\"title\":\"%s\"}",
                task.getId(), task.getTitle());
    }

    public static String tasksToJson(TaskService taskService) {
        String json = "[";
        for (Task task : taskService.getTasks()) {
            json += taskToJson(task) + ",";
        }

        json = json.substring(0, json.lastIndexOf(","));

        return json + "]";
    }
}
