package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TaskControllerWebTest {
    private static final Long TEST_ID = 1L;
    private static final Long TEST_NOT_EXSIT_ID = 100L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 목록_조회 {
        @Test
        @DisplayName("200을 반환한다")
        void it_responses_200() throws Exception {
            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 상세_조회 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 존재하는_id일_경우 {
            @Test
            @DisplayName("200를 반환한다")
            void it_responses_200() throws Exception {
                given(taskService.getTask(TEST_ID)).willReturn(new Task());

                mockMvc.perform(get("/tasks/" + TEST_ID))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 존재하지_않는_id일_경우 {
            @Test
            @DisplayName("예외를 던진다")
            void it_responses_404() throws Exception {
                given(taskService.getTask(TEST_NOT_EXSIT_ID))
                        .willThrow(new TaskNotFoundException(TEST_NOT_EXSIT_ID));

                mockMvc.perform(get("/tasks/" + TEST_NOT_EXSIT_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
