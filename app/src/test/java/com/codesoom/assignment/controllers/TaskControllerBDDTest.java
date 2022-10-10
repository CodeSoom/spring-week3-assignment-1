package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.appllication.TaskService;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TaskControllerBDDTest {

    private TaskService taskService;
    private TaskController taskController;
    private Task task;
    private Task source;
    private final String TEST_TITLE = "TEST_TITLE";
    private final String UPDATE_TITLE = "UPDATE_TITLE";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        task = new Task(1L, TEST_TITLE);
        taskController.create(task);
    }

    @Nested
    @DisplayName("Get 메소드는")
    class Describe_get {

        @Nested
        @DisplayName("id가 없다면")
        class Context_have_no_id {

            @Test
            @DisplayName("taskList를 리턴한다")
            void it_returns_taskList() {
                assertThat(taskController.getTaskList()).hasSize(1);
            }
        }

        @Nested
        @DisplayName("id가 있다면")
        class Context_have_id {

            @Test
            @DisplayName("특정 id task객체를 리턴한다.")
            void it_returns_taskList() {
                assertThat(taskController.getTaskById(1L)).isEqualTo(new Task(1L, TEST_TITLE));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id라면")
        class Context_nonexistent_id {

            @Test
            @DisplayName("TaskNotFoundException을 발생시킨다.")
            void it_returns_TaskNotFoundException() {
                assertThatThrownBy(() -> taskController.getTaskById(-1L)).isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("Post 메소드는")
    class Describe_post {

        @Nested
        @DisplayName("Task객체가 주어질 때")
        class Context_have_task {

            @BeforeEach
            void setUp() {
                source = new Task();
                source.updateTitle("Create_Task");
            }

            @Test
            @DisplayName("task를 리턴한다.")
            void it_returns_task() {
                assertThat(taskController.create(source).getTitle()).isEqualTo(source.getTitle());
            }
        }
    }

    @Nested
    @DisplayName("Put 메소드는")
    class Describe_put {

        @Nested
        @DisplayName("task객체의 title을 update했을 때")
        class Context_update_taskTitle {

            @BeforeEach
            void setUp() {
                source = new Task(2L,TEST_TITLE);
                taskController.create(source);
                source.updateTitle(UPDATE_TITLE);
            }

            @Test
            @DisplayName("title이 update된 task객체를 리턴한다.")
            void it_returns_updateTask() {
                assertThat(taskController.put(2L, source).getTitle()).isEqualTo(UPDATE_TITLE);
            }
        }
    }

    @Nested
    @DisplayName("Delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("id가 주어질 때")
        class Context_have_id {

            @Test
            @DisplayName("그 id를 가진 task객체를 삭제 후, 삭제한 객체를 리턴해준다")
            void it_returns_deleteTask() {
                assertThat(taskController.delete(1L)).isEqualTo(task);
            }
        }
    }

}

