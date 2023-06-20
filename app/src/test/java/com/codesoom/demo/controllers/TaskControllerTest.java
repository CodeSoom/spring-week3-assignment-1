package com.codesoom.demo.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.controllers.TaskController;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;
    private static final String TASK_TITLE = "test";

    @BeforeEach
    void setup(){
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("새로운 Task를 생성하면")
        class Context_with_create_task {

            private Task task;
            private int oldTasksSize;
            private int newTasksSize;

            @BeforeEach
            void setup(){
                oldTasksSize = taskService.getTasks().size();

                Task source = new Task();
                source.setTitle(TASK_TITLE);
                task = taskService.createTask(source);

                newTasksSize = taskService.getTasks().size();
            }

            @Test
            @DisplayName("새로 추가된 Task를 리턴한다")
            void it_returns_a_created_task() {
                assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
            }

            @Test
            @DisplayName("Tasks 리스트 길이가 1 증가한다")
            void it_returns_list_size_increased_by_one() {
                assertThat(newTasksSize - oldTasksSize).isEqualTo(1);
            }

        }
    }





}