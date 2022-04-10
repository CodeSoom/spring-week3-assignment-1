package com.codesoom.assignment.controllers.tasks;


import com.codesoom.assignment.domains.Task;
import com.codesoom.assignment.domains.TaskDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskUpdateControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long EXIST_ID;
    private static final Long NOT_EXIST_ID = 0L;

    @BeforeEach
    void setup() throws Exception {
        final ResultActions actions = mockMvc.perform(post("/tasks")
                .content(objectMapper.writeValueAsString(new TaskDto("title")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        final Task task = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString().getBytes(), Task.class);
        this.EXIST_ID = task.getId();
    }

    @DisplayName("성공적으로 할 일을 변경한다.")
    @Test
    void updateTask() throws Exception {
        mockMvc.perform(patch("/tasks/" + EXIST_ID)
                .content(objectMapper.writeValueAsString(new TaskDto("new title")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("new title")));
    }

    @DisplayName("존재하지 않는 할 일을 수정하려고 하면 404를 반환한다.")
    @Test
    void notFound() throws Exception {
        mockMvc.perform(patch("/tasks/" + NOT_EXIST_ID)
                .content(objectMapper.writeValueAsString(new TaskDto("new title")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("빈 값으로 할 일을 수정할 경우 예외가 발생한다.")
    @Test
    void emptyTask() throws Exception {
        for (String title : Arrays.asList("", " ", null)) {
            mockMvc.perform(patch("/tasks/" + EXIST_ID)
                    .content(objectMapper.writeValueAsString(new TaskDto(title)))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

}
