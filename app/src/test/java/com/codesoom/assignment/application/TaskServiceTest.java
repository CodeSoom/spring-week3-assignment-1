package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("TaskService 클래스")
public class TaskServiceTest {

    private TaskService taskService;
    private Task task;

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "!!!";

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        task = new Task();
        task.setTitle(TASK_TITLE);
    }

    @Nested
    @DisplayName("getTasks 호출은")
    class Describe_getTasks {

        @Nested
        @DisplayName("task가 저장돼 있다면")
        class Context_with_tasks {

            @BeforeEach
            void setUp() {
                taskService.createTask(task);
                taskService.createTask(task);
            }

            @Test
            @DisplayName("task list를 리턴한다")
            void it_returns_task_list() {
                assertThat(taskService.getTasks()).hasSize(2);
            }
        }

        @Nested
        @DisplayName("저장된 task가 없다면")
        class Context_with_empty_tasks {

            @Test
            @DisplayName("빈 task list를 리턴한다")
            void it_returns_empty_task_list() {
                assertThat(taskService.getTasks()).isEmpty();
            }
        }
    }

}
