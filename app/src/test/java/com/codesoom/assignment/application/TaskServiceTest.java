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
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskService 클래스")
class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    private Task generateTask(long id, String title) {
        Task newTask = new Task();
        newTask.setId(id);
        newTask.setTitle(title);
        return newTask;
    }

    private TaskService generateTaskService(long taskCount) {
        TaskService newTaskService = new TaskService();
        for (long i = 1L; i <= taskCount; i++) {
            Task newTask = generateTask(i, String.format("task%d", i));
            newTaskService.createTask(newTask);
        }
        return newTaskService;
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_of_getTasks {

        @Nested
        @DisplayName("만약 tasks가 비어있다면")
        class Context_of_empty_tasks {

            @Test
            @DisplayName("비어있는 배열을 반환한다")
            void it_returns_empty_array() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).isEmpty();
            }
        }

        @Nested
        @DisplayName("만약 tasks가 비어있지 않다면")
        class Context_of_tasks_not_empty {

            private TaskService size1;
            private TaskService size2;
            private TaskService size100;

            @BeforeEach
            void setup() {
                size1 = generateTaskService(1);
                size2 = generateTaskService(2);
                size100 = generateTaskService(100);
            }

            @Test
            @DisplayName("tasks에 포함된 모든 객체를 반환한다")
            void it_returns_all_tasks() {
                assertThat(size1.getTasks()).hasSize(1);
                assertThat(size2.getTasks()).hasSize(2);
                assertThat(size100.getTasks()).hasSize(100);
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_of_getTask {

        private Task task1;
        private Task task2;
        private Long currentTaskId = 0L;

        @BeforeEach
        void append_two_task_to_tasks() {
            currentTaskId += 1;
            task1 = generateTask(currentTaskId, "task" + currentTaskId);
            currentTaskId += 1;
            task2 = generateTask(currentTaskId, "task" + currentTaskId);

            taskService.createTask(task1);
            taskService.createTask(task2);
        }

        @Nested
        @DisplayName("만약 tasks에 존재하지 않는 객체 id를 인자로 입력하면")
        class Context_of_non_existent_id {

            private Task notAppendedTask;
            private long nonExistentId;

            @BeforeEach
            void setNonExistentId() {
                notAppendedTask = generateTask(1L, "notAppended");
                nonExistentId = notAppendedTask.getId();
            }

            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throws_task_not_found_exception() {
                assertThatThrownBy(() -> taskService.getTask(nonExistentId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 tasks에 포함된 객체의 id를 인자로 전달 받으면")
        class Context_of_exist_id {

            private long existId;

            @BeforeEach
            void setExistId() {
                existId = task1.getId();
            }

            @Test
            @DisplayName("id에 해당하는 task를 반환한다")
            void getTask() {
                Task task = taskService.getTask(existId);

                assertThat(task).isEqualTo(task1);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_of_createTask {

        @Nested
        @DisplayName("만약 Task 객체를 인자로 전달받으면")
        class Context_of_given_task {

            private Task givenTask;

            @BeforeEach
            void setGivenTask() {
                Long currentTaskId = 1L;
                givenTask = generateTask(currentTaskId, "task" + currentTaskId);
            }

            @Test
            @DisplayName("새 객체를 생성 및 tasks에 추가하고, 추가한 객체를 반환한다")
            void it_appends_new_task_and_returns_it() {
                Task createdTask = taskService.createTask(givenTask);
                assertThat(createdTask)
                        .isEqualTo(givenTask)
                        .withFailMessage("추가한 객체가 반환되지 않았다");

                createdTask = taskService.getTask(givenTask.getId());
                assertThat(createdTask)
                        .isEqualTo(givenTask)
                        .withFailMessage("새 객체가 tasks에 추가되지 않았다");
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_of_updateTask {

        @Nested
        @DisplayName("만약 id와 Task 객체가 인자로 주어지면")
        class Context_of_input_task {

            private Task givenTask;
            private long givenTaskId;

            @BeforeEach
            void append_task_and_modify_title_of_given_task() {
                givenTaskId = 1L;
                givenTask = generateTask(givenTaskId, "task" + givenTaskId);

                taskService.createTask(givenTask);
                givenTask.setTitle("modifed_title");
            }

            @Test
            @DisplayName("기존 객체를 갱신한 후, 갱신한 객체를 반환한다 ")
            void it_updates_task_and_returns_it() {
                Task updatedTask = taskService.updateTask(givenTaskId, givenTask);

                assertThat(updatedTask).isEqualTo(givenTask)
                        .withFailMessage("갱신한 객체를 적절히 반환하지 않았다");

                updatedTask = taskService.getTask(givenTask.getId());
                assertThat(updatedTask).isEqualTo(givenTask)
                        .withFailMessage("기존 객체를 갱신하지 않았다");
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_of_deleteTask {

        private Task givenTask;
        private Long currentTaskId = 0L;

        @BeforeEach
        void append_a_task_to_tasks() {
            currentTaskId += 1;
            givenTask = generateTask(currentTaskId, "task" + currentTaskId);

            Task createdTask = taskService.createTask(givenTask);
            currentTaskId = createdTask.getId();
        }

        @Nested
        @DisplayName("만약 tasks에 존재하지 않는 객체 id를 인자로 전달하면")
        class Context_of_non_existent_id {

            private Long nonExistentId;

            @BeforeEach
            void setNonExistentId() {
                taskService.deleteTask(givenTask.getId());

                nonExistentId = givenTask.getId();
            }

            @Test
            @DisplayName("TaskNotFoundException을 던진다")
            void it_throws_TaskNotFoundException() {
                assertThatThrownBy(() -> taskService.getTask(nonExistentId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 tasks에 존재하는 객체의 id를 인자로 전달하면")
        class Context_of_exist_id {

            private Long existId;

            @BeforeEach
            void setExistId() {
                existId = currentTaskId;
            }

            @Test
            @DisplayName("해당 id 객체를 tasks에서 제거하고, 제거한 객체를 반환한다")
            void it_removes_task_from_tasks_and_returns_it() {
                Task deletedTask = taskService.deleteTask(existId);

                assertThat(deletedTask).isEqualTo(givenTask)
                        .withFailMessage("삭제한 객체가 반환되지 않았다");
                assertThatThrownBy(() -> taskService.getTask(existId))
                        .isInstanceOf(TaskNotFoundException.class)
                        .withFailMessage("객체가 삭제되지 않았다");
            }
        }
    }

}
