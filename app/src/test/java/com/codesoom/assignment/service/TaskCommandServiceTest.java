package com.codesoom.assignment.service;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskCommandService의")
class TaskCommandServiceTest {

    public static final String TASK_TITLE = "Test Title";
    private TaskCommandService taskCommandService;
    private TaskQueryService taskQueryService;

    @BeforeEach
    void setUp() {
        TaskRepository taskRepository = new TaskRepository();
        taskQueryService = new TaskQueryService(taskRepository);
        taskCommandService = new TaskCommandService(taskRepository, taskQueryService);
        Task task = new Task(1L, TASK_TITLE);
        taskCommandService.createTask(task);
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {

        int oldSize;
        int newSize;
        Task newTask;

        @BeforeEach
        void createSetUp() {
            oldSize = taskQueryService.getTaskList().size();
            newTask = new Task(2L, "second");
        }

        @Test
        @DisplayName("새로운 할 일을 등록한다.")
        void createTask() {
            taskCommandService.createTask(newTask);
            newSize = taskQueryService.getTaskList().size();

            Task findNewTask = taskQueryService.getTask(2L);

            assertThat(newSize - oldSize).isEqualTo(1);
            assertThat(findNewTask).isNotNull();
            assertThat(findNewTask.getId()).isEqualTo(newTask.getId());
            assertThat(findNewTask.getTitle()).isEqualTo(newTask.getTitle());
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {

        String newTitle;
        Task source;

        @BeforeEach
        void updateSetUp() {
            newTitle = "New Title";
            source = new Task(null, newTitle);
        }

        @Test
        @DisplayName("할 일을 수정한다.")
        void updateTask() {
            taskCommandService.updateTask(1L, source);
            Task task = taskQueryService.getTask(1L);
            assertThat(task.getTitle()).isEqualTo(newTitle);
        }
    }

    @Nested
    @DisplayName("completeTask 메소드는")
    class Describe_completeTask {
        @Test
        @DisplayName("할 일을 리스트에서 제외한다.")
        void completeTask() {
            taskCommandService.completeTask(1L);
            assertThatThrownBy(() -> {
                Task task = taskQueryService.getTask(1L);
            }).isInstanceOf(TaskNotFoundException.class);
        }
    }

}