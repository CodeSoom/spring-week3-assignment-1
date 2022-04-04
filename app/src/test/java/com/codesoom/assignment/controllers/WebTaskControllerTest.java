package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("할 일에 대한 MockMVC 테스트")
@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc
public class WebTaskControllerTest {

    private static final String TEST_TASK_TITLE = "테스트";

    private static final Long NOT_FOUND_TASK_ID = 9999L;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("할 일 목록 조회")
    void list_200() throws Exception {
        //given
        int givenSize = 10;
        List<Task> givenTasks = IntStream.rangeClosed(1, givenSize)
                .mapToObj(index -> {
                    Task task = new Task();
                    task.setId((long) index);
                    task.setTitle(TEST_TASK_TITLE + "_" + index);
                    return task;
                })
                .collect(Collectors.toList());

        given(taskService.getTasks()).willReturn(givenTasks);

        //when
        ResultActions resultActions = mockMvc.perform(get("/tasks"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(givenSize)))
                .andDo(print());
    }

    @Test
    @DisplayName("할 일 등록")
    void create_201() throws Exception {
        //given
        Long taskId = 1L;
        Task source = new Task();
        source.setTitle(TEST_TASK_TITLE);

        Task task = new Task();
        task.setId(taskId);
        task.setTitle(TEST_TASK_TITLE);

        given(taskService.createTask(any())).willReturn(task);

        //when
        ResultActions resultActions = mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(source)));

        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(String.valueOf(taskId)))
                .andExpect(jsonPath("title").value(TEST_TASK_TITLE));
    }

    @Test
    @DisplayName("할 일 상세 조회")
    void detail_200() throws Exception {
        //given
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle(TEST_TASK_TITLE);

        given(taskService.getTask(taskId)).willReturn(task);

        //when
        ResultActions resultActions = mockMvc.perform(get("/tasks/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(String.valueOf(taskId)))
                .andExpect(jsonPath("title").value(TEST_TASK_TITLE));
    }

    @Test
    @DisplayName("할 일 상세 조회시 404 응답")
    void detail_NotFound() throws Exception {
        //given
        given(taskService.getTask(anyLong())).willThrow(TaskNotFoundException.class);

        //when
        ResultActions resultActions = mockMvc.perform(get("/tasks/{taskId}", NOT_FOUND_TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("message").exists());
    }

    @Test
    @DisplayName("할일 삭제")
    void delete_204() throws Exception {
        //given
        given(taskService.deleteTask(anyLong())).willReturn(null);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/tasks/{taskId}", NOT_FOUND_TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("할 일 삭제시 404 응답")
    void delete_404() throws Exception {
        //given
        given(taskService.deleteTask(any())).willThrow(TaskNotFoundException.class);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/tasks/{taskId}", NOT_FOUND_TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("할 일 대체")
    void update_200() throws Exception {
        //given
        Task source = new Task();
        source.setTitle(TEST_TASK_TITLE);

        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle(TEST_TASK_TITLE);

        given(taskService.updateTask(any(), any())).willReturn(task);

        //when
        ResultActions resultActions = mockMvc.perform(put("/tasks/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(source))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").exists());
    }

    @Test
    @DisplayName("할 일 대체시 404 응답")
    void update_404() throws Exception {
        //given
        Task source = new Task();
        source.setTitle(TEST_TASK_TITLE);

        given(taskService.updateTask(any(), any())).willThrow(TaskNotFoundException.class);

        //when
        ResultActions resultActions = mockMvc.perform(put("/tasks/{taskId}", NOT_FOUND_TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(source))
        );

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("할 일 변경")
    void patch_200() throws Exception {
        //given
        Task source = new Task();
        source.setTitle(TEST_TASK_TITLE);

        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle(TEST_TASK_TITLE);

        given(taskService.updateTask(any(), any())).willReturn(task);

        //when
        ResultActions resultActions = mockMvc.perform(patch("/tasks/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(source))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").exists());
    }

    @Test
    @DisplayName("할 일 변경시 404 응답")
    void patch_404() throws Exception {
        //given
        Task source = new Task();
        source.setTitle(TEST_TASK_TITLE);

        given(taskService.updateTask(any(), any())).willThrow(TaskNotFoundException.class);

        //when
        ResultActions resultActions = mockMvc.perform(patch("/tasks/{taskId}", NOT_FOUND_TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(source))
        );

        //then
        resultActions.andExpect(status().isNotFound());
    }
}
