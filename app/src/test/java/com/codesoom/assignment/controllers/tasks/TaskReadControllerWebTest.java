package com.codesoom.assignment.controllers.tasks;

import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.domains.TaskDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskReadControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long EXIST_ID;
    private static final Long NOT_EXIST_ID = 100L;
    private static final String EXIST_TITLE = "title";

    @BeforeEach
    void setup() throws Exception {
        final ResultActions actions = mockMvc.perform(post("/tasks")
                .content(objectMapper.writeValueAsString(new TaskDto(EXIST_TITLE)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        final Task task = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString().getBytes(), Task.class);
        this.EXIST_ID = task.getId();
    }

    @DisplayName("모든 할 일을 성공적으로 조회한다.")
    @Test
    void getTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXIST_TITLE)));;
    }

    @DisplayName("id로 할 일을 성공적으로 조회한다.")
    @Test
    void getTask() throws Exception {
        mockMvc.perform(get("/tasks/" + EXIST_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXIST_TITLE)));
    }

    @DisplayName("요청한 id에 해당하는 값이 없으면 404를 반환한다.")
    @Test
    void getTaskNotFoundExceptionTest() throws Exception {
        mockMvc.perform(get("/tasks/" + NOT_EXIST_ID))
                .andExpect(status().isNotFound());
    }

}