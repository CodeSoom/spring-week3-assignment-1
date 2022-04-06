package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.domains.TaskDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TaskDeleteControllerWebTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long EXIST_ID = 1L;
    private static final Long NOT_EXIST_ID = 100L;

    @BeforeEach
    void setup() throws Exception {
        mockMvc.perform(post("/tasks")
                .content(objectMapper.writeValueAsString(new TaskDto("title")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("title")));
    }

    @DisplayName("id로 할 일을 성공적으로 삭제한다.")
    @Test
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/" + EXIST_ID))
                .andExpect(status().isNoContent());
    }

    @DisplayName("존재하지 않는 id로 삭제 요청을 하면, 404를 반환한다.")
    @Test
    void thrownNotFoundException() throws Exception {
        mockMvc.perform(delete(String.format("/tasks/%s", NOT_EXIST_ID)))
                .andExpect(status().isNotFound());
    }

}