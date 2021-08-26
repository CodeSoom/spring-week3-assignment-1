package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@DisplayName("TaskControllerWebTest 클래스")
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    private final String[] TASK_TITLE = {"","test1", "test2", "test3", "test4", "test5"};
    private final String TASK_UPDATE = "update";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskService taskService; // 스프링에서 DI 주입

    @BeforeEach
    @DisplayName("Task 객체 초기화")
    void setUp() {

        List<Task> tasks = new ArrayList<>();

        Task task1 = new Task();
        task1.setTitle(TASK_TITLE[1]);

        Task task2 = new Task();
        task2.setTitle(TASK_TITLE[2]);

        tasks.add(task1);
        tasks.add(task2);

        // list
        given(taskService.getTasks()).willReturn(tasks);

        // id가 존재합니다.
        given(taskService.getTask(1L)).willReturn(task1);

        // id가 존재하지 않습니다.
        given(taskService.getTask(100L)).willThrow(TaskNotFoundException.class);

        // task 객체를 추가합니다.
        Task task3 = new Task();
        task3.setTitle(TASK_TITLE[3]);
        given(taskService.createTask(task3)).willReturn(task3);

        // task를 수정합니다.
        Task updateTask = new Task();
        updateTask.setTitle(TASK_TITLE[1] + TASK_UPDATE);
        given(taskService.updateTask(1L, updateTask)).willReturn(updateTask);

    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("tasks 리스트에 값이 존재하면")
        class Context_exist_tasks {

            @Test
            @DisplayName("task 객체들을 리턴하고 200 상태코드를 내려준다")
            void It_return_tasks_status_200() throws Exception {

                mockMvc.perform(get("/tasks")).andExpect(status().isOk());

            }

        }

    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("list에 id가 존재하면")
        class Context_exist_id {

            @Test
            @DisplayName("Task 객체를 리턴해주고 200 상태코드를 내려준다")
            void It_return_task_status_200() throws Exception {

                mockMvc.perform(get("/tasks/1")).andExpect(status().isOk());

            }

        }

        @Nested
        @DisplayName("list에 id가 존재하지 않는다면")
        class Context_exist_not_id {

            @Test
            @DisplayName("TaskNotFoundException 예외를 던져주고 404 에러를 내려준다")
            void It_return_exception_status_404() throws Exception {

                mockMvc.perform(get("/tasks/100")).andExpect(status().isNotFound());

            }

        }

    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("수정하고 싶은 id가 존재하면")
        class Context_exist_update_id {

            @Test
            @DisplayName("list에서 id를 찾아 값을 수정한다")
            void It_update() throws Exception {

                mockMvc.perform(put("/tasks/1")).andExpect(status().isOk());

            }

        }

    }

}
