package com.codesoom.assignment.web;

import com.codesoom.assignment.constant.TaskConstant;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Task source;
    private Task updateSource;

    @BeforeEach
    void setup() {
        source = new Task(TaskConstant.TITLE);
        updateSource = new Task(TaskConstant.UPDATE_TITLE);
    }

    @Test
    @DisplayName("할 일 항목 가져오기")
    void getTaskList() throws Exception {
        // when
        mockMvc.perform(get("/tasks"))

        // then
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("할 일 생성")
    void createTask() throws Exception {
        // when
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(source)))

        // then
        .andExpect(status().isCreated())
        .andExpect(jsonPath("title").value(TaskConstant.TITLE));
    }

    @Test
    @DisplayName("할 일 가져오기")
    void getTask() throws Exception {
        // given
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(source)));

        // when
        mockMvc.perform(get("/tasks/" + TaskConstant.ID))

        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("title").value(TaskConstant.TITLE));
    }

    @Test
    @DisplayName("할 일 가져오기 - 존재하지 않을 경우")
    void getNotExistsTask() throws Exception {
        // given
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(source)));

        // when
        mockMvc.perform(get("/tasks/" + TaskConstant.NOT_EXISTS_ID))

        // then
        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("할 일 수정 - PUT")
    void modifyTask() throws Exception {
        // given
        MvcResult mvcResult = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(source)))
                        .andReturn();
        String taskId = taskId(mvcResult);

        // when
        mockMvc.perform(put("/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateSource)))

        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("title").value(TaskConstant.UPDATE_TITLE));
    }

    @Test
    @DisplayName("할 일 수정 - PATCH")
    void modifyTaskByPatch() throws Exception {
        // given
        MvcResult mvcResult = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(source)))
                        .andReturn();
        String taskId = taskId(mvcResult);

        // when
        mockMvc.perform(patch("/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateSource)))

        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("title").value(TaskConstant.UPDATE_TITLE));
    }

    @Test
    @DisplayName("할 일 삭제")
    void deleteTask() throws Exception {
        // given
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(source)));

        // when
        mockMvc.perform(delete("/tasks/" + TaskConstant.ID))

        // then
        .andExpect(status().isNoContent());
    }

    private String taskId(MvcResult mvcResult) throws UnsupportedEncodingException {
        return Integer.toString(JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("id"));
    }
}
