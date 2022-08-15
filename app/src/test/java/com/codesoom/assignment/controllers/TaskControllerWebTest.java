package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
@DisplayName("TaskController 클래스의")
class TaskControllerWebTest {
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Long givenId1 = 1L;
    private final Long givenId2 = 2L;
    private final String givenTodo1 = "Todo1";
    private final String givenTodo2 = "Todo2";
    private final String givenTitleToChange = "변경 후";

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new TaskController(taskService))
                .setControllerAdvice(TaskErrorAdvice.class)
                .build();

        reset(taskService);
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @Nested
        @DisplayName("빈 작업 목록이 주어졌을 때")
        class Context_with_empty_list {
            @Test
            @DisplayName("빈 배열과 상태코드 200을 보낸다")
            void It_return_empty_list_and_status_ok() throws Exception {
                given(taskService.getTasks()).willReturn(new ArrayList<>());

                mockMvc.perform(get("/tasks"))
                        .andExpect(jsonPath("$").isEmpty())
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("작업을 가진 목록이 주어지면")
        class Context_with_list {
            @Test
            @DisplayName("작업 목록과 상태코드 200을 보낸다")
            void It_returns_list_and_ok() throws Exception {
                given(taskService.getTasks())
                        .willReturn(Arrays.asList(
                                new Task(givenId1, givenTodo1),
                                new Task(givenId2, givenTodo2)));

                mockMvc.perform(get("/tasks"))
                        .andExpect(jsonPath("$.[0].title").value(givenTodo1))
                        .andExpect(jsonPath("$.[1].title").value(givenTodo2))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("식별자가 주어지고 해당 식별자를 가진 작업이 있다면")
        class Context_with_id_and_task {
            @Test
            @DisplayName("작업을 리턴한다")
            void It_returns_task() throws Exception {
                given(taskService.getTask(givenId1)).willReturn(new Task(givenId1, givenTodo1));

                mockMvc.perform(get("/tasks/1"))
                        .andExpect(jsonPath("$.id").value(givenId1))
                        .andExpect(jsonPath("$.title").value(givenTodo1))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("작업을 찾지 못했다면")
        class Context_without_task {
            @Test
            @DisplayName("에러 응답과 상태 코드 404를 리턴합니다")
            void It_returns_error_response_and_not_found() throws Exception {
                given(taskService.getTask(givenId1)).willThrow(TaskNotFoundException.class);

                mockMvc.perform(get("/tasks/1"))
                        .andExpect(jsonPath("$.message").value("Task not found"))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("제목이 주어지면")
        class Context_with_title {
            @Test
            @DisplayName("식별자를 가진 작업을 리턴한다")
            void It_returns_task_with_id() throws Exception {
                Map<String, String> input = new HashMap<>();
                input.put("title", givenTodo1);
                given(taskService.createTask(givenTodo1)).willReturn(new Task(givenId1, givenTodo1));

                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(input))
                        )
                        .andExpect(jsonPath("$.id").value(givenId1))
                        .andExpect(jsonPath("$.title").value(givenTodo1))
                        .andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        @Nested
        @DisplayName("주어진 식별자를 갖는 작업이 있을 때")
        class Context_when_exists_task {
            @Test
            @DisplayName("작업의 제목을 변경하고 상태코드 200을 리턴한다")
            void It_change_title_and_return() throws Exception {
                Map<String, String> input = new HashMap<>();
                input.put("title", givenTitleToChange);

                given(taskService.updateTask(givenId1, givenTitleToChange)).willReturn(new Task(givenId1, givenTitleToChange));

                mockMvc.perform(put("/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(input)))
                        .andExpect(jsonPath("$.id").value(givenId1))
                        .andExpect(jsonPath("$.title").value(givenTitleToChange))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_patch {
        @Nested
        @DisplayName("주어진 식별자를 갖는 작업이 있을 때")
        class Context_when_exists_task {
            @Test
            @DisplayName("작업의 제목을 변경하고 상태코드 200을 리턴한다")
            void It_change_title_and_return() throws Exception {
                Map<String, String> input = new HashMap<>();
                input.put("title", givenTitleToChange);

                given(taskService.updateTask(givenId1, givenTitleToChange)).willReturn(new Task(givenId1, givenTitleToChange));

                mockMvc.perform(patch("/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(input)))
                        .andExpect(jsonPath("$.id").value(givenId1))
                        .andExpect(jsonPath("$.title").value(givenTitleToChange))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        @Nested
        @DisplayName("주어진 식별자를 가진 작업이 있으면")
        class Context_with_task {
            @Test
            @DisplayName("작업을 삭제하고 상태코드 204를 리턴한다")
            void It_delete_task_and_returns_no_content() throws Exception {
                doNothing().when(taskService).deleteTask(givenId1);

                mockMvc.perform(delete("/tasks/1"))
                        .andExpect(status().isNoContent());

                verify(taskService, times(1)).deleteTask(givenId1);
            }
        }
    }
}
