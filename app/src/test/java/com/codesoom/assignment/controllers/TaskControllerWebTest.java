package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskController Web 테스트입니다.")
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    public static final String TEST_3 = "test3";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private Task test1;
    private Task test2;
    private Task test3;
    private static final String TASK_UPDATE_TITLE = "updateTitle";


    @BeforeEach
    void setUp() {
        test1 = new Task(1L, "test1");
        test2 = new Task(2L, "test2");
        test3 = Task.from("test3");

        given(taskService.getTasks()).willReturn(Arrays.asList(test1, test2));
        given(taskService.getTask(1L))
                .willReturn(test1);

        given(taskService.getTask(10L))
                .willThrow(new TaskNotFoundException(10L));

        given(taskService.createTask(test3))
                .willReturn(new Task(3L, TEST_3));

        given(taskService.updateTask(1L, Task.from(TASK_UPDATE_TITLE)))
                .willReturn(new Task(1L, TaskControllerWebTest.TASK_UPDATE_TITLE));

        given(taskService.updateTask(10L, Task.from(TASK_UPDATE_TITLE)))
                .willThrow(new TaskNotFoundException(10L));


        given(taskService.deleteTask(3L))
                .willThrow(new TaskNotFoundException(3L));

    }


    @DisplayName("할 일 전체 조회를 할 수 있습니다 - GET /tasks")
    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test1")));
    }

    @DisplayName("할 일을 조회할 수 있습니다. - GET /tasks/{id}")
    @Test
    void detail() throws Exception {
        mockMvc.perform(get("/tasks/" + test1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("test1"))
                .andReturn();
    }

    @DisplayName("잘못된 식별자로 할 일을 조회하면 실패합니다 - GET /tasks/{invalidId}")
    @Test
    void detailInvalid() throws Exception {
        mockMvc.perform(get("/tasks/10"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("할 일을 생성할 수 있습니다. - POST /tasks")
    @Test
    void createTask() throws Exception {
        final String contents = objectMapper.writeValueAsString(test3);

        mockMvc.perform(post("/tasks")
                .content(contents)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.title").value(TEST_3));
    }


}
