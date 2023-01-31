package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    private static final String TASK_TITLE = "Test1";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Test
    void getList() {
        assertThat(taskService.getTasks()).hasSize(1);
    }

    @Test
    void getFoundDetail() {
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getNotFoundDetail() {
        assertThatThrownBy(() -> taskService.getTask(404L)).isInstanceOf(TaskNotFoundException.class);
    }

    @DisplayName("새로운 Task를 생성한다.")
    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();

        Task source = new Task();
        source.setTitle("Test2");

        taskService.createTask(source);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }


    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTast {
        private String updateTitle = "Test2";
        private Task source;

        @BeforeEach
        void setUp() {
            source = new Task();
            source.setTitle(updateTitle);
        }

        @Nested
        @DisplayName("만약 등록되어 있는 Id와 새로운 제목이 주어진다면")
        class updateValidTask {

            @Test
            @DisplayName("등록 된 Id를 새로운 제목으로 업데이트 한다")
            void updateTask() {

                Task task = taskService.updateTask(1L, source);

                assertThat(task.getTitle()).isEqualTo(updateTitle);
            }
        }

        @Nested
        @DisplayName("만약 등록되어 있지 않은 Id와 새로운 제목이 주어진다면")
        class updateInvalidTask {

            @Test
            @DisplayName("찾을 수 없다는 예외를 던진다")
            void updateNotFoundTask() {
                assertThatThrownBy(() -> taskService.updateTask(404L, source))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

    }

    @Test
    void deleteTaskSomething() {
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(1L);

        int newSize = taskService.getTasks().size();

        assertThat(newSize - oldSize).isEqualTo(0);
    }

    @Test
    void deleteTaskNothing() {
        assertThatThrownBy(() -> taskService.deleteTask(404L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
