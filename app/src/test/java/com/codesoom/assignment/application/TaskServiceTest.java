package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Nested
@DisplayName("TaskService 클래스의")
class TaskServiceTest {

    private TaskService taskService;

    private Task firstTask;
    private Task secondTask;

    @BeforeEach
    void setUp() {
        // subject
        this.taskService = new TaskService();

        // fixtures
        Task task1 = new Task();
        task1.setTitle("test1");

        Task task2 = new Task();
        task2.setTitle("test2");

        this.firstTask = this.taskService.createTask(task1);
        this.secondTask = this.taskService.createTask(task2);
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {

        @Nested
        @DisplayName("주어진 id에 해당하는 할 일이 있다면")
        class Context_with_existing_task {

            @Test
            @DisplayName("할 일을 반환한다")
            void it_returns_a_task() {
                assertThat(taskService.getTask(firstTask.getId()).getTitle())
                        .isEqualTo(firstTask.getTitle());
            }
        }

        @Nested
        @DisplayName("주어진 id에 해당하는 할 일이 없다면")
        class Context_with_no_task {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던진다")
            void it_returns_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(10L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Test
    void testGetTasks() {
        assertThat(this.taskService.getTasks()).isNotEmpty();
    }

    @Test
    void testGetTask() {
        assertThat(this.taskService.getTask(this.firstTask.getId()).getTitle())
                .isEqualTo(this.firstTask.getTitle());
        assertThatThrownBy(() -> taskService.getTask(10L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void testCreateTask() {
        Task task3 = new Task();
        assertThat(this.taskService.createTask(task3).getTitle()).isNull();
    }

    @Test
    void testUpdateTask() {
        String updatedTitle = "updated title";
        Task src = new Task();
        src.setId(this.firstTask.getId());
        src.setTitle(updatedTitle);
        assertThat(this.taskService.updateTask(this.firstTask.getId(), src).getTitle())
                .isEqualTo(updatedTitle);
    }

    @Test
    void testDeleteTask() {
        assertThat(this.taskService.deleteTask(this.firstTask.getId()).getId())
                .isEqualTo(this.firstTask.getId());
        assertThatThrownBy(() -> taskService.deleteTask(this.firstTask.getId()))
                .isInstanceOf(TaskNotFoundException.class);
        assertThat(this.taskService.deleteTask(this.secondTask.getId()).getTitle())
                .isEqualTo(this.secondTask.getTitle());
    }
}
