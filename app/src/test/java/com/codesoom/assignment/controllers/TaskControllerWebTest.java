package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.TaskRequest;
import com.codesoom.assignment.dto.TaskResponse;
import com.codesoom.assignment.exceptions.BadRequestException;
import com.codesoom.assignment.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Nested
    class getTasks_메서드는 {
        @BeforeEach
        void setUp() {
            List<TaskResponse> taskResponses = new ArrayList<>();
            taskResponses.add(new TaskResponse(1L, "과제하기"));
            taskResponses.add(new TaskResponse(2L, "밥먹기"));

            given(taskService.getTasks()).willReturn(taskResponses);
        }

        @Test
        void status_code_200을_응답한다() throws Exception {
            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].title", is("과제하기")))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].title", is("밥먹기")));
        }
    }

    @Nested
    class getTask_메서드는 {
        @BeforeEach
        void setUp() {
            taskService.addTask(new TaskRequest("과제하기"));
        }

        @Nested
        class 만약_유효한_id가_주어진다면 {
            @BeforeEach
            void setUp() {
                Long id = 1L;
                given(taskService.getTask(id)).willReturn(new TaskResponse(id, "과제하기"));
            }

            @Test
            void status_code_200을_응답한다() throws Exception {
                mockMvc.perform(get("/tasks/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", is(1)))
                        .andExpect(jsonPath("$.title", is("과제하기")));
            }
        }

        @Nested
        class 만약_유효하지_않은_id가_주어진다면 {
            @BeforeEach
            void setUp() {
                Long id = 100L;
                given(taskService.getTask(id)).willThrow(new NotFoundException(id));
            }

            @Test
            void status_code_404를_응답한다() throws Exception {
                mockMvc.perform(get("/tasks/100"))
                        .andExpect(status().isNotFound())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
            }
        }
    }

    @Nested
    class create_메서드는 {
        @Nested
        class 만약_유효한_title이_주어진다면 {
            @BeforeEach
            void setUp() {
                given(taskService.addTask(new TaskRequest("과제하기"))).willReturn(new TaskResponse(1L, "과제하기"));
            }

            @Test
            void status_code_201을_응답한다() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequest = objectMapper.writeValueAsString(new TaskRequest("과제하기"));

                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id", is(1L)))
                        .andExpect(jsonPath("$.title", is("과제하기")));
            }
        }

        @Nested
        class 만약_유효하지_않은_title이_주어진다면 {
            @BeforeEach
            void setUp() {
                given(taskService.addTask(new TaskRequest(""))).willThrow(new BadRequestException());
            }

            @Test
            void status_code_400을_응답한다() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequest = objectMapper.writeValueAsString(new TaskRequest(""));

                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isBadRequest())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException));
            }
        }
    }

    @Nested
    class update_메서드는 {
        @BeforeEach
        void setUp() {
            taskService.addTask(new TaskRequest("과제하기"));
        }

        @Nested
        class 만약_유효하지_않은_id가_주어진다면 {
            @BeforeEach
            void setUp() {
                Long id = 100L;
                given(taskService.updateTask(id, new TaskRequest("춤추기"))).willThrow(new NotFoundException(id));
            }

            @Test
            void status_code_404를_응답한다() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequest = objectMapper.writeValueAsString(new TaskRequest("춤추기"));

                mockMvc.perform(put("/tasks/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isNotFound())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
            }
        }

        @Nested
        class 만약_유효하지_않은_title이_주어진다면 {
            @BeforeEach
            void setUp() {
                Long id = 1L;
                given(taskService.updateTask(id, new TaskRequest(""))).willThrow(new NotFoundException(id));
            }

            @Test
            void status_code_400을_응답한다() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequest = objectMapper.writeValueAsString(new TaskRequest(""));

                mockMvc.perform(put("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isBadRequest())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException));
            }
        }

        @Nested
        class 만약_유효한_id와_title이_주어진다면 {
            @BeforeEach
            void setUp() {
                Long id = 1L;
                given(taskService.updateTask(id, new TaskRequest("춤추기"))).willThrow(new NotFoundException(id));
            }

            @Test
            void status_code_200을_응답한다() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequest = objectMapper.writeValueAsString(new TaskRequest("춤추기"));

                mockMvc.perform(put("/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", is(1)))
                        .andExpect(jsonPath("$.title", is("춤추기")));
            }
        }
    }

    @Nested
    class patch_메서드는 {
        @BeforeEach
        void setUp() {
            taskService.addTask(new TaskRequest("과제하기"));
        }

        @Nested
        class 만약_유효하지_않은_id가_주어진다면 {
            @BeforeEach
            void setUp() {
                Long id = 100L;
                given(taskService.updateTask(id, new TaskRequest("춤추기"))).willThrow(new NotFoundException(id));
            }

            @Test
            void status_code_404를_응답한다() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequest = objectMapper.writeValueAsString(new TaskRequest("춤추기"));

                mockMvc.perform(patch("/tasks/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isNotFound())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
            }
        }

        @Nested
        class 만약_유효하지_않은_title이_주어진다면 {
            @BeforeEach
            void setUp() {
                Long id = 1L;
                given(taskService.updateTask(id, new TaskRequest(""))).willThrow(new NotFoundException(id));
            }

            @Test
            void status_code_400을_응답한다() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequest = objectMapper.writeValueAsString(new TaskRequest(""));

                mockMvc.perform(patch("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isBadRequest())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException));
            }
        }

        @Nested
        class 만약_유효한_id와_title이_주어진다면 {
            @BeforeEach
            void setUp() {
                Long id = 1L;
                given(taskService.updateTask(id, new TaskRequest("춤추기"))).willThrow(new NotFoundException(id));
            }

            @Test
            void status_code_200을_응답한다() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequest = objectMapper.writeValueAsString(new TaskRequest("춤추기"));

                mockMvc.perform(patch("/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", is(1)))
                        .andExpect(jsonPath("$.title", is("춤추기")));
            }
        }
    }

    @Nested
    class delete_메서드는 {
        @BeforeEach
        void setUp() {
            taskService.addTask(new TaskRequest("과제하기"));
        }

        @Nested
        class 만약_유효하지_않은_id가_주어진다면 {
            @BeforeEach
            void setUp() {
                Long id = 100L;
                doThrow(new NotFoundException(id)).when(taskService).deleteTask(id);
            }

            @Test
            void status_code_404를_응답한다() throws Exception {
                mockMvc.perform(delete("/tasks/100"))
                        .andExpect(status().isNotFound())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
            }
        }

        @Nested
        class 만약_유효한_id가_주어진다면 {
            @Test
            void status_code_204를_응답한다() throws Exception {
                mockMvc.perform(delete("/tasks/1"))
                        .andExpect(status().isNoContent());
            }
        }
    }
}
