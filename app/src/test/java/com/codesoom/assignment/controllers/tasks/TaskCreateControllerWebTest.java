package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.domains.TaskDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TaskCreateControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TASK_TITLE = "task";
    private static final TaskDto VALID_TASK_DTO = new TaskDto(TASK_TITLE);
    private static final TaskDto EMPTY_TASK_DTO = new TaskDto("");

    @DisplayName("할 일을 성공적으로 추가한다.")
    @Test
    void addTask() throws Exception {
        mockMvc.perform(post("/tasks")
                .content(objectMapper.writeValueAsString(VALID_TASK_DTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(TASK_TITLE)))
                .andDo(print());
    }

    @DisplayName("빈 값의 할 일을 추가하면 400 에러를 응답한다.")
    @Test
    void emptyTask() throws Exception {
        mockMvc.perform(post("/tasks")
                .content(objectMapper.writeValueAsString(EMPTY_TASK_DTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("request body가 없으면 400 에러를 응답한다.")
    @Test
    void nullRequestBody() throws Exception {
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}