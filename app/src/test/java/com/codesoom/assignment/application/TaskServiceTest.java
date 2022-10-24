package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Nested
    class WhenEmpty {

        @Test
        @DisplayName("get all tasks")
        void getTasks() {
            assertThat(taskService.getTasks()).isEmpty();
        }

        @Test
        @DisplayName("get a task")
        void getTask() {
            final Long randomId = getRandomId();

            assertThatThrownBy(() -> taskService.getTask(randomId))
                    .isInstanceOf(TaskNotFoundException.class);
        }

        Long getRandomId() {
            long id = new Random().nextLong();
            return id < 0 ? -id : id;
        }
    }

}