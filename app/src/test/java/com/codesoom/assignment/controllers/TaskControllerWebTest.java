package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    ObjectMapper objectMapper;

    private final static String NEW_TITLE = "new task";
    private final static String TITLE_POSTFIX = " spring";

    @BeforeEach
    void setUp() {
        Long taskId = 1L;
        Long wrongId = 100L;

        Task task = new Task();
        task.setId(taskId);
        task.setTitle(NEW_TITLE);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(taskId)).willReturn(task);
        given(taskService.getTask(wrongId)).willThrow(TaskNotFoundException.class);
    }

    @DisplayName("할일 목록을 조회하면 200 코드를 받을 수 있다")
    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[")))
                .andExpect(content().string(containsString("]")));
    }

    @DisplayName("할일을 조회하면 200 코드를 받을 수 있다")
    @Test
    void detail_ok() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(NEW_TITLE)));
    }

    @DisplayName("잘못된 식별값으로 할일을 조회하면 404 코드를 받을 수 있다")
    @Test
    void detail_fail() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("할일을 생성하면 200 코드를 받을 수 있다")
    @Test
    void create() throws Exception {
        Task source = new Task();
        source.setTitle(NEW_TITLE);
        String content = objectMapper.writeValueAsString(source);

        given(taskService.createTask(any(Task.class))).willReturn(source);

        mockMvc.perform(post("/tasks")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(NEW_TITLE)));
    }

    @DisplayName("할일을 수정하면 200 코드를 받을 수 있다")
    @Test
    void update() throws Exception {
        Long taskId = 1L;
        Task source = new Task();
        source.setTitle(NEW_TITLE + TITLE_POSTFIX);
        String content = objectMapper.writeValueAsString(source);

        given(taskService.updateTask(eq(taskId), any(Task.class))).willReturn(source);

        mockMvc.perform(patch("/tasks/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(NEW_TITLE + TITLE_POSTFIX)));
    }

    @DisplayName("할일을 삭제하면 204 코드를 받을 수 있다")
    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
