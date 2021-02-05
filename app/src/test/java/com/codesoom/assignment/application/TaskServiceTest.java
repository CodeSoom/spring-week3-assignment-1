package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskServiceTest {

    TaskService taskService;

    @BeforeEach
    void setup() {
        taskService = new TaskService();
    }

    private Task createSampleTask() {
        Task task = new Task();
        task.setTitle("old title");
        return taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("Task가 존재할 때")
        class Context_exist_task {
            @Test
            @DisplayName("Task의 List를 반한다")
            void it_return_list() {
                createSampleTask();

                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).isNotEmpty();
                assertThat(tasks).hasSize(1);
            }
        }

        @Nested
        @DisplayName("Task가 존재하지 않을 때")
        class Context_does_not_exist_task {
            @Test
            @DisplayName("빈 리스트를 반환한다")
            void it_return_empty_list() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).isEmpty();
                assertThat(tasks).hasSize(0);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("id가 존재할 때")
        class Context_exist_id {
            @Test
            @DisplayName("Task를 반한다")
            void it_return_task() {
                Task createTask = createSampleTask();

                Task getTask = taskService.getTask(1L);

                assertThat(getTask.getId()).isEqualTo(createTask.getId());
                assertThat(getTask.getTitle()).isEqualTo(createTask.getTitle());
                assertThat(getTask.getClass()).isEqualTo(createTask.getClass());
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않을 때")
        class Context_does_not_exist_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_return_exception() {
                assertThrows(TaskNotFoundException.class, () -> taskService.getTask(3L));
            }
        }
    }

    @Test
    @DisplayName("createTask()")
    void createTask() {
        Task task = new Task();
        task.setTitle("타이틀1");
        taskService.createTask(task);
        assertEquals(taskService.getTasks().size(), 1);

        Task task2 = new Task();
        task.setTitle("타이틀2");
        taskService.createTask(task2);
        assertEquals(taskService.getTasks().size(), 2);
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        Task updateTask;

        @BeforeEach
        void setup(){
            updateTask = new Task();
            updateTask.setTitle("new title");
        }

        @Nested
        @DisplayName("변경할 task의 title과 id가 존재할 때")
        class Context_exist_task_and_id {
            @Test
            @DisplayName("id에 해당하는 Task의 title을 변경하고 반환한다.")
            void it_return_task() {
                createSampleTask();

                taskService.updateTask(1L, updateTask);

                assertThat(taskService.getTask(1L).getTitle()).isEqualTo("new title");
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않을 때")
        class Context_does_not_exist_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_return_exception() {
                assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(3L, updateTask));
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("변경할 id가 존재할 때")
        class Context_exist_task_and_id {
            @Test
            @DisplayName("id에 해당하는 Task를 삭제하고 반환한다.")
            void it_return_task() {
                createSampleTask();
                assertEquals(taskService.getTasks().size(), 1);

                taskService.deleteTask(1L);

                assertEquals(taskService.getTasks().size(), 0);
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않을 때")
        class Context_does_not_exist_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_return_exception() {
                assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(3L));
            }
        }
    }

}
