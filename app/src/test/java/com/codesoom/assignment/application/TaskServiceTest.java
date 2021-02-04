package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class TaskServiceTest {
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)

    private static final String TASK_TITLE = "test";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // subject
        taskService = new TaskService();

        // fixture
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_list {

        @Test
        @DisplayName("Task가 존재하면 하나 이상의 Task 목록을 반환한다 ")
        void getTasks() {
            List<Task> tasks = taskService.getTasks();

            assertThat(tasks).hasSize(1);

            Task task = tasks.get(0);
            assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
        }
    }

    @Nested
    @DisplayName("ID가 존재할 경우")
    class Describe_id {

        @Test
        @DisplayName("해당 ID를 갖는 Task를 반환하고")
        void getTaskWithValidId() {
            Task task = taskService.getTask(1L);
            assertThat(task.getTitle()).isEqualTo("test");
        }

        @Nested
        @DisplayName("ID가 존재하지 않는 경우")
        class Describe_noId {

            @Test
            @DisplayName("Task를 찾을 수 없다는 경고 메시지를 반환한다")
            void getTaskWithInValidId() {
                assertThatThrownBy(() -> taskService.getTask(100L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task task1 = new Task();
        Task task2 = new Task();

        ArrayList<Task> tasks = new ArrayList<>();

        tasks.add(task1);
        tasks.add(task2);

        assertThat(tasks.size()).isEqualTo(2);
        assertThat(tasks.size() - oldSize).isEqualTo(1);
    }
}
