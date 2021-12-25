package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "New";

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks는")
    class Describe_getTasks {
        @Test
        @DisplayName("등록된 전체 리스트를 리턴한다.")
        void getTasksList() {
            List<Task> tasks = taskService.getTasks();

            assertThat(tasks).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("getTask는 ")
    class Describe_getTask {
        @Test
        @DisplayName("id에 해당하는 Task가 존재하면 Task를 리턴한다.")
        void getTaskWithExistId() {
            Task task = taskService.getTask(1L);

            assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
        }

        @Test
        @DisplayName("id에 해당하는 Task가 존재하지않으면 Task를 찾을 수 없다는 예외를 던진다.")
        void getTaskWithNonexistId() {
            assertThatThrownBy(() -> taskService.getTask(0L))
                    .isInstanceOf(TaskNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("CreateTask는")
    class Describe_createTask {
        @Test
        @DisplayName("Task를 생성하고, 리턴한다.")
        void createTask() {
            int oldSize = taskService.getTasks().size();

            Task task = new Task();
            task.setTitle(TASK_TITLE);

            taskService.createTask(task);

            int newSize = taskService.getTasks().size();

            assertThat(newSize - oldSize).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("deleteTask는")
    class Describe_deleteTask {
        @Test
        void deleteTask() {
            int oldSize = taskService.getTasks().size();

            taskService.deleteTask(1L);

            int newSize = taskService.getTasks().size();

            assertThat(oldSize - newSize).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("updateTask는")
    class Describe_updateTask {
        @Test
        @DisplayName("해당 id의 Task를 수정하고 리턴한다.")
        void updateTask() {
            Task source = new Task();
            source.setTitle(UPDATE_POSTFIX + TASK_TITLE);
            taskService.updateTask(1L, source);

            Task task = taskService.getTask(1L);
            assertThat(task.getTitle()).isEqualTo(UPDATE_POSTFIX + TASK_TITLE);
        }
    }
}
