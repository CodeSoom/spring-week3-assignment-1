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
    final Long ID = 1L;
    final Long DOES_NOT_EXIST_ID = 3L;
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

            @BeforeEach
            void setUp() {
                createSampleTask();
            }

            @Test
            @DisplayName("Task의 List를 반환한다")
            void it_return_list() {
                List<Task> tasks = taskService.getTasks();

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
            @DisplayName("Task를 반환한다")
            void it_return_task() {
                Task sampleTask = createSampleTask();

                Task task = taskService.getTask(ID);

                assertThat(task.getId()).isEqualTo(sampleTask.getId());
                assertThat(task.getTitle()).isEqualTo(sampleTask.getTitle());
                assertThat(task.getClass()).isEqualTo(sampleTask.getClass());
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않을 때")
        class Context_does_not_exist_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_return_exception() {
                assertThrows(TaskNotFoundException.class, () -> taskService.getTask(DOES_NOT_EXIST_ID));
            }
        }
    }

    @Test
    @DisplayName("createTask()")
    void createTask() {
        //given
        int originSize = taskService.getTasks().size();

        //when
        Task task = new Task();
        task.setTitle("타이틀1");
        taskService.createTask(task);

        //then
        assertThat(taskService.getTasks().size() - originSize).isEqualTo(1);
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        Task task;

        @BeforeEach
        void setup() {
            task = new Task();
            task.setTitle("new title");
        }

        @Nested
        @DisplayName("변경할 task의 title과 id가 존재할 때")
        class Context_exist_task_and_id {
            @BeforeEach
            void setUp() {
                createSampleTask();
            }

            @Test
            @DisplayName("id에 해당하는 Task의 title을 변경하고 반환한다.")
            void it_return_task() {
                taskService.updateTask(ID, task);

                assertThat(taskService.getTask(ID).getTitle()).isEqualTo("new title");
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않을 때")
        class Context_does_not_exist_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_return_exception() {
                assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(DOES_NOT_EXIST_ID, task));
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        @Nested
        @DisplayName("변경할 id가 존재할 때")
        class Context_exist_task_and_id {
            @BeforeEach
            void setUp() {
                createSampleTask();
            }

            @Test
            @DisplayName("id에 해당하는 Task를 삭제하고 반환한다.")
            void it_return_task() {
                int originSize = taskService.getTasks().size();

                taskService.deleteTask(ID);

                assertEquals(originSize - taskService.getTasks().size(), 1);
            }
        }

        @Nested
        @DisplayName("id가 존재하지 않을 때")
        class Context_does_not_exist_id {
            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_return_exception() {
                assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(DOES_NOT_EXIST_ID));
            }
        }
    }

}
