package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {
    final String TASK_TITLE = "Get Sleep";
    final String UPDATE_TITLE = "Do Study";

    TaskController taskController;
    TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    Task createTask() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        return taskService.createTask(task);
    }

    @Nested
    @DisplayName("list()")
    class Describe_list {
        @Nested
        @DisplayName("task가 존재한다면")
        class Context_task_exist {
            @BeforeEach
            void givenTaskCreate() {
                createTask();
            }

            @DisplayName("task 리스트를 반환한다")
            @Test
            void it_return_task_list() {
                //when
                List<Task> list = taskController.list();
                //then
                assertThat(list).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("task가 존재하지 않는다면")
        class Context_task_not_exist {
            @DisplayName("비어있는 리스트 반환한다")
            @Test
            void it_return_empty_list() {
                //when
                List<Task> list = taskController.list();
                //then
                assertThat(list).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("detail()")
    class Describe_detail {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_with_exist_task_id {
            Task givenTask;

            @BeforeEach
            void givenTaskCreate() {
                givenTask = createTask();
            }

            @DisplayName("주어진 id와 일치하는 task를 반환한다")
            @Test
            void it_return_task_list() {
                //when
                Task task = taskController.detail(givenTask.getId());
                //then
                assertThat(task).isNotNull();
                assertThat(task.getId()).isEqualTo(givenTask.getId());
                assertThat(task.getTitle()).isEqualTo(givenTask.getTitle());
            }
        }

        @Nested
        @DisplayName("task가 존재하지 않는다면")
        class Context_task_not_exist {
            @DisplayName("TaskNotFoundException를 던진다")
            @Test
            void it_return_exception() {
                //when
                //then
                assertThrows(TaskNotFoundException.class, () -> taskController.detail(100L),
                        "주어진 ID에 해당하는 Task 없습니다");
            }
        }
    }

    @Nested
    @DisplayName("create()")
    class Describe_create {

        @DisplayName("주어진 id와 일치하는 task를 반환한다")
        @Test
        void it_return_task_list() {
            //given
            Task givenTask = new Task();
            givenTask.setTitle(TASK_TITLE);
            //when
            Task task = taskController.create(givenTask);
            //then
            assertThat(task).isNotNull();
            assertThat(task.getTitle()).isEqualTo(givenTask.getTitle());
        }
    }

    @Nested
    @DisplayName("update()")
    class Describe_update {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_with_exist_task_id {
            Task givenTask;
            @BeforeEach
            void setUp() {
                givenTask = createTask();
            }

            @DisplayName("title이 변경된 task를 반환한다")
            @Test
            void it_return_task_list() {
                //when
                givenTask.setTitle(UPDATE_TITLE);
                Task updatedTask = taskController.update(givenTask.getId(), givenTask);
                //then
                assertThat(updatedTask).isNotNull();
                assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TITLE);
                assertThat(updatedTask.getId()).isEqualTo(givenTask.getId());
            }
        }

        @Nested
        @DisplayName("task가 존재하지 않는다면")
        class Context_task_not_exist {
            @DisplayName("TaskNotFoundException를 던진다")
            @Test
            void it_return_exception() {
                //given
                Task task = new Task();
                task.setTitle(UPDATE_TITLE);
                //when
                //then
                assertThrows(TaskNotFoundException.class, () -> taskController.update(100L, task),
                        "주어진 ID에 해당하는 Task 없습니다");
            }
        }
    }

    @Nested
    @DisplayName("patch()")
    class Describe_patch {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_with_exist_task_id {
            Task givenTask;
            @BeforeEach
            void setUp() {
                givenTask = createTask();
            }

            @DisplayName("title이 변경된 task를 반환한다")
            @Test
            void it_return_task_list() {
                //when
                givenTask.setTitle(UPDATE_TITLE);
                Task updatedTask = taskController.patch(givenTask.getId(), givenTask);
                //then
                assertThat(updatedTask).isNotNull();
                assertThat(updatedTask.getTitle()).isEqualTo(UPDATE_TITLE);
                assertThat(updatedTask.getId()).isEqualTo(givenTask.getId());
            }
        }

        @Nested
        @DisplayName("task가 존재하지 않는다면")
        class Context_task_not_exist {
            @DisplayName("TaskNotFoundException를 던진다")
            @Test
            void it_return_exception() {
                //given
                Task task = new Task();
                task.setTitle(UPDATE_TITLE);
                //when
                //then
                assertThrows(TaskNotFoundException.class, () -> taskController.patch(100L, task),
                        "주어진 ID에 해당하는 Task 없습니다");
            }
        }
    }

    @Nested
    @DisplayName("delete()")
    class Describe_delete {
        @Nested
        @DisplayName("존재하는 task id가 주어진다면")
        class Context_with_exist_task_id {
            Task givenTask;

            @BeforeEach
            void givenTaskCreate() {
                givenTask = createTask();
            }

            @DisplayName("주어진 id와 일치하는 task를 삭제한다")
            @Test
            void it_return_task_list() {
                //when
                taskController.delete(givenTask.getId());
                //then
                List<Task> list = taskController.list();
                Optional<Task> task =
                        list.stream().filter(e -> e.getId().equals(givenTask.getId())).findFirst();
                assertThat(task).isEmpty();
            }
        }

        @Nested
        @DisplayName("task가 존재하지 않는다면")
        class Context_task_not_exist {
            @DisplayName("TaskNotFoundException를 던진다")
            @Test
            void it_return_exception() {
                //when
                //then
                assertThrows(TaskNotFoundException.class, () -> taskController.delete(100L),
                        "주어진 ID에 해당하는 Task 없습니다");
            }
        }
    }
}

@FunctionalInterface
interface TaskUpdater {
    Task updateTask(long id, Task task);
}
