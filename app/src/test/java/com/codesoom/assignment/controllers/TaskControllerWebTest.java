package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스")
public class TaskControllerWebTest {
    final long TASK_ID = 0L;
    final String TASK_TITLE = "Get Sleep";
    final String UPDATE_TITLE = "Do Study";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    TaskService taskService;

    @AfterEach
    void clear() {
        Mockito.reset(taskService);
    }

    Task createTask(String title) throws Exception {
        Task task = new Task();
        task.setTitle(title);
        task.setId(TASK_ID);
        return task;
    }

    @Nested
    @DisplayName("GET 메소드 리퀘스트는")
    class Describe_GET {
        @Nested
        @DisplayName("tasks가 비어있다면")
        class Context_empty_tasks {
            @Test
            @DisplayName("비어있는 리스트 리턴한다")
            void it_returns_empty_array() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"));
            }
        }

        @Nested
        @DisplayName("tasks가 비어있지 않다면")
        class Context_fill_task {
            List<Task> givenTasks;

            @BeforeEach
            void givenTaskCreate() throws Exception {
                Task task = createTask(TASK_TITLE);
                givenTasks = Collections.singletonList(task);
                given(taskService.getTasks()).willReturn(givenTasks);
            }

            @Test
            @DisplayName("task 리스트를 리턴한다")
            void it_returns_empty_array() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TASK_TITLE)))
                        .andExpect(content().string(objectMapper.writeValueAsString(givenTasks)));
            }
        }

        @Nested
        @DisplayName("존재하는 task id를 요청한다면")
        class Context_exist_id {
            Task givenTask;

            @BeforeEach
            void givenTaskCreate() throws Exception {
                givenTask = createTask(TASK_TITLE);
                given(taskService.getTask(TASK_ID)).willReturn(givenTask);
            }

            @Test
            @DisplayName("task 객체를 리턴한다")
            void it_returns_task_object() throws Exception {
                mockMvc.perform(get("/tasks/0"))
                        .andExpect(status().isOk())
                        .andExpect(content().json(objectMapper.writeValueAsString(givenTask)));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 task id를 요청한다면")
        class Context_not_exist_id {

            @Test
            @DisplayName("404코드와 에러메시지를 리턴한다")
            void it_returns_not_found() throws Exception {
                mockMvc.perform(get("/tasks/100"))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
