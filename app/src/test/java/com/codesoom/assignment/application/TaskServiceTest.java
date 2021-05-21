package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("TaskService")
class TaskServiceTest {
    private TaskService taskService;
    private List<Task> tasks;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);

//        taskService = new TaskService();
    }

    private Task makingTask(int index) {
        Task task = new Task();
        task.setId(Long.valueOf(index));
        task.setTitle("First Title "+index);
        return task;
    }

    @Nested
    @DisplayName("getTasks() 메소드는")
    class Describe_getTasks {
        @Test
        @DisplayName("Task 목록이 비어 있으면 비어있는 목록을 반환한다..")
        public void get_empty_tasks()  {
            List<Task> tasks = taskService.getTasks();
            assertThat(tasks.size()).isEqualTo(0);
            assertThat(tasks).isEmpty();

        }

        public void get_not_empty_tasks(){
            tasks = mock(List.class);
            tasks.add(makingTask(1));
            tasks.add(makingTask(2));
            given(taskService.getTasks()).willReturn(tasks);

            List<Task> newTasks =taskService.getTasks();

            assertThat(newTasks).isNotEmpty();
        }
    }


}