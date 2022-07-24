package com.codesoom.assignment;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@TestConfiguration
public class TestConfig {
    @Bean
    public TaskService taskService() {
        return new TaskService() {
            private Long id = 0L;
            private final Map<Long, Task> taskMap = new HashMap<>();

            @Override
            public Collection<Task> getTasks() {
                return taskMap.values();
            }

            @Override
            public Task getTask(Long id) {
                return Optional.ofNullable(taskMap.get(id))
                        .orElseThrow(() -> new TaskNotFoundException(id));
            }

            @Override
            public Task createTask(String title) {
                Task task = new Task(id, title);
                taskMap.put(id++, task);
                return task;
            }

            @Override
            public Task updateTask(Long id, String title) {
                return getTask(id).changeTitle(title);
            }

            @Override
            public void deleteTask(Long id) {
                taskMap.remove(id);
            }
        };
    }
}
