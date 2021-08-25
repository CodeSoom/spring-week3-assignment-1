package com.codesoom.assignment.controllers;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Task Controller WebTest는")
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        ArrayList<Task> tasks = new ArrayList<>();
        Task task = new Task(1L, "title");
        tasks.add(task);

        given(taskService.getTaskList()).willReturn(tasks);
        given(taskService.getTask(1L)).willReturn(task);
        given(taskService.createTask(task)).willReturn(task);
    }

    @Nested
    @DisplayName("할 일(id)에 대한 조회 요청을 했을 때")
    class GetTask {
        @Test
        @DisplayName("전체 리스트 요청일 경우 OK를 응답한다.")
        void list() throws Exception {
            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("title")));
        }

        @Test
        @DisplayName("정상 요청이면 OK를 응답한다.")
        void detailWithValidId() throws Exception {
            mockMvc.perform(get("/tasks/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("잘못된 아이디를 요청하면 Not Found를 응답한다.")
        void detailWithInvalidId() throws Exception {
            given(taskService.getTask(10000L))
                    .willThrow(new TaskNotFoundException("10000"));

            mockMvc.perform(get("/tasks/10000"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("비정상적인 PathVariable을 요청하면 Not Found를 응답한다.")
        void detailWithInvalidPathVariable() throws Exception {
            mockMvc.perform(get("/tasks/undefined"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("할 일(id)에 대한 등록 요청이 왔을 때")
    class CreateTask {
        @Test
        @DisplayName("요청이 정상적인 경우 할 일을 등록한다.")
        void createTask() throws Exception {
            mockMvc.perform(post("/tasks"))
                    .andExpect(status().isCreated());
        }
    }




}
