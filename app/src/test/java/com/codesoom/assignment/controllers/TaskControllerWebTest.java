package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskControllerWebTest 클래스는")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;

    private final Long TASK_ID = 1L;
    private final Long TASK_UNKNOWN_ID = 100L;
    private final String TASK_TITLE_ONE = "test_One";
    private final String TASK_TITLE_TWO = "test_Two";

    private final List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Task task = new Task();
        task.setTitle(TASK_TITLE_ONE);
        tasks.add(task);
    }

    @AfterEach
    void clear() {
        tasks.clear();
    }

    @Nested
    class list_메서드는 {
        @BeforeEach
        void setUp() {
            given(taskService.getTasks()).willReturn(tasks);
        }

        @Test
        void HTTP_Status_Code_200으로_응답한다() throws Exception {
            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class Detail_메서드는 {
        @Nested
        class 만약_존재하는_Task에_대한_조회를_한다면 {
            @BeforeEach
            void setUp() {
                final Task task = tasks.get(0);
                given(taskService.getTask(TASK_ID)).willReturn(task);
            }

            @Test
            void HTTP_Status_Code_200으로_응답한다() throws Exception {
                mockMvc.perform(get("/tasks/" + TASK_ID))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        class 만약_존재하지_않는_Task에_대한_조회를_한다면 {
            @BeforeEach
            void setUp() {
                given(taskService.getTask(TASK_UNKNOWN_ID))
                        .willThrow(new TaskNotFoundException(TASK_UNKNOWN_ID));
            }

            @Test
            void HTTP_Status_Code_404로_응답한다() throws Exception {
                mockMvc.perform(get("/tasks/" + TASK_UNKNOWN_ID))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    class Update_메서드는 {
        @Nested
        class 만약_존재하는_Task를_수정한다면 {
            @BeforeEach
            void setUp() {
                final Task task = tasks.get(0);
                task.setTitle(TASK_TITLE_TWO);

                given(taskService.updateTask(TASK_ID, new Task(TASK_TITLE_TWO))).willReturn(task);
            }

            @Test
            void HTTP_Status_Code_200으로_응답한다() throws Exception {
//                    mockMvc.perform(put("/tasks/" + TASK_ID)
//                                .content(convertToJsonString(new Task(TASK_TITLE_TWO)))
//                                .contentType(MediaType.APPLICATION_JSON)
//                        ).andExpect(status().isOk());
            }
        }
    }

    private String convertToJsonString(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }

}
