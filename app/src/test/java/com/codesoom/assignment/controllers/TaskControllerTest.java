package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TEST_TITLE = "test";
    private static final String TEST_POSTFIX = "_!!!";

    @BeforeEach
    void setUp() {
        List<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setTitle(TEST_TITLE);
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(1L)).willReturn(task);

        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));

        when(taskService.createTask(any(Task.class)))
                .then((arg) -> {
                    Task source = arg.getArgument(0, Task.class);
                    Task responseTask = new Task();
                    responseTask.setId(1L);
                    responseTask.setTitle(source.getTitle());
                    return responseTask;
                });

        when(taskService.updateTask(any(Long.class), any(Task.class)))
                .then((arg) -> {
                    Long targetId = arg.getArgument(0, Long.class);
                    Task source = arg.getArgument(1, Task.class);

                    if (targetId.equals(100L)) {
                        throw new TaskNotFoundException(100L);
                    }

                    Task responseTask = new Task();
                    responseTask.setId(targetId);
                    responseTask.setTitle(source.getTitle());
                    return responseTask;
                });

        when(taskService.deleteTask(any(Long.class)))
                .then((arg) -> {
                    Long targetId = arg.getArgument(0, Long.class);

                    if (targetId.equals(100L)) {
                        throw new TaskNotFoundException(100L);
                    }

                    return taskService.getTask(targetId);
                });
    }

    @Test
    @DisplayName("Task 전체 리스트 요청 확인")
    void listTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("존재하는 Task ID로 Task 정보 요청")
    void detailTaskWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(TEST_TITLE))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 Task ID로 Task 정보 요청")
    void detailTaskWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Task 생성 요청")
    void createTask() throws Exception {
        String sourceTaskContent = getSourceTaskContent();

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourceTaskContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(TEST_TITLE + TEST_POSTFIX))
                .andDo(print());
    }

    @Test
    @DisplayName("PUT 요청으로 Task 업데이트")
    void updateTaskOfHttpPut() throws Exception {
        String sourceTaskContent = getSourceTaskContent();

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourceTaskContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(TEST_TITLE + TEST_POSTFIX))
                .andDo(print());
    }

    @Test
    @DisplayName("PATCH 요청으로 Task 업데이트")
    void updateTaskOfHttpPatch() throws Exception {
        String sourceTaskContent = getSourceTaskContent();

        mockMvc.perform(patch("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourceTaskContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(TEST_TITLE + TEST_POSTFIX));
    }

    @Test
    @DisplayName("존재하지 않는 Task ID로 Task 업데이트")
    void updateTaskOfHttpPutWithInvalidId() throws Exception {
        String sourceTaskContent = getSourceTaskContent();

        mockMvc.perform(put("/tasks/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourceTaskContent))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하는 Task ID로 Task 삭제")
    void deleteTaskWithValidId() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 Task ID로 Task 삭제")
    void deleteTaskWithInvalidId() throws Exception {
        mockMvc.perform(delete("/tasks/100"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private String getSourceTaskContent() throws JsonProcessingException {
        Task source = new Task();
        source.setTitle(TEST_TITLE + TEST_POSTFIX);

        return objectMapper.writeValueAsString(source);
    }
}
