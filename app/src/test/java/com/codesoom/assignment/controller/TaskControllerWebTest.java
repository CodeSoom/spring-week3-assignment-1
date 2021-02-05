package com.codesoom.assignment.controller;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    @Autowired // 우리가 new를 하지 않고 spring이 자동으로 넣어주는 것
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService; // 실제가 아닌 가짜인데, 진짜처럼 작동하게 해서 테스트함

    List<Task> tasks;
    Task task;

    private final String TASK_TITLE = "Test Task";
    private final long TASK_ID = 1L;
    private final long NOT_EXISTING_TASK_ID = 100L;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
        task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
    }

    @Nested
    @DisplayName("GET 요청은")
    class Describe_get_request {
        @Nested
        @DisplayName("할 일 목록에 저장된 데이터가 있으면")
        class Context_with_tasks {
            @BeforeEach
            void setUp() {
                tasks.add(task);
                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("200 코드를 응답하고, 저장 되어있는 할 일을 리턴한다.")
            void It_respond_200_and_tasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("Test Task")));
            }
        }

        @Nested
        @DisplayName("할 일 목록에 저장된 데이터가 없으면")
        class Context_with_no_task {
            @BeforeEach
            void setUp() {
                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("200 코드를 응답하고, 비어있는 목록을 리턴한다.")
            void it_respond_200_and_tasks() throws Exception {
                mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"));
            }
        }

        @Nested
        @DisplayName("할 일 목록에 존재하는 id로 조회한다면")
        class Context_contains_target_id {
            @BeforeEach
            void setUp() {
                tasks.add(task);
                given(taskService.getTask(TASK_ID)).willReturn(task);
            }

            @Test
            @DisplayName("200 코드를 응답하고, id에 일치하는 할 일을 리턴한다.")
            void It_respond_200_and_target_task() throws Exception {
                mockMvc.perform(get("/tasks/{id}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").value(TASK_ID))
                        .andExpect(jsonPath("title").value(TASK_TITLE));
            }
        }

        @Nested
        @DisplayName("할 일 목록에 존재하지 않는 id로 조회한다면")
        class Context_not_contains_target_id {
            @BeforeEach
            void setUp() {
                given(taskService.getTask(NOT_EXISTING_TASK_ID))
                        .willThrow(new TaskNotFoundException(NOT_EXISTING_TASK_ID));
            }

            @Test
            @DisplayName("404 코드를 응답한다.")
            void It_respond_404() throws Exception {
                mockMvc.perform(get("/tasks/{id}", NOT_EXISTING_TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("id").doesNotExist());
            }
        }
    }

    @Nested
    @DisplayName("POST 요청은")
    class Describe_post_request {
        @Nested
        @DisplayName("새로운 할 일을 추가하면")
        class Context_add_new_task {
            @BeforeEach
            void setUp() {
                Task newTask = new Task();
                newTask.setId(1004L);
                newTask.setTitle("Just love yourself");
                given(taskService.createTask(any(Task.class))).willReturn(newTask);
            }

            @Test
            @DisplayName("201 코드를 응답하고, 생성된 할 일을 리턴한다.")
            void create() throws Exception {
                mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(task)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("id").value(1004L))
                        .andExpect(jsonPath("title").exists())
                        .andExpect(jsonPath("title").value("Just love yourself"));
            }
        }
    }
}
