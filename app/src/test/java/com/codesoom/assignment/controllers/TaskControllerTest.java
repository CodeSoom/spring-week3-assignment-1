package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Nested
    @DisplayName("list 메소드")
    class method_of_list {

        @Nested
        @DisplayName("if tasks isEmpty")
        class if_tasks_isEmpty {
            @Test
            @DisplayName("return []")
            void return_empty_list(){
                List<Task> tasks = taskService.getTasks();
                assertThat(tasks).isEmpty();
            }
        }
        @Nested
        @DisplayName("if tasks is not Empty")
        class if_tasks_isNotEmpty {
            @Test
            @DisplayName("return tasks")
            void it_returns_all_tasks() {
                Task task1 = generateTask(1L, "task1");
                Task task2 = generateTask(2L, "task2");

                taskService.createTask(task1);
                taskService.createTask(task2);
                List<Task> tasks = taskService.getTasks();
                // id값이 달라져서 다르다고 나와서 해당 사항을 이렇게 테스트했습니다.
                assertThat(tasks.get(0).getTitle()).isEqualTo(task1.getTitle());
                assertThat(tasks.get(1).getTitle()).isEqualTo(task2.getTitle());
                assertThat(tasks.size()).isEqualTo(2);
            }
        }

    }

    private Task generateTask(long id, String title) {
        Task newTask = new Task();
        newTask.setId(id);
        newTask.setTitle(title);
        return newTask;
    }

    @Test
    @DisplayName("테스트는 초기값이 0이다.")
    public void list(){
        TaskController taskController = new TaskController(taskService);
        assertThat(taskController.list()).isEmpty();
    }

    @Test
    public void create(){
        TaskController taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle("hello");

        taskController.create(task);

        assertThat(taskController.list()).isNotEmpty();
    }
}