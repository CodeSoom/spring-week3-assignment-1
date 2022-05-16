package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private Task task;
    private final String TASK_TITLE = "babo";
    private final Long TASK_ID = 1L;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle(TASK_TITLE);
        task.setId(TASK_ID);
    }

    @Nested
    @DisplayName("GET /tasks")
    class TasksGet {

        @DisplayName("tasks를 응답한다.")
        @Test
        void list() throws Exception {
            List<Task> tasks = new ArrayList<>();
            tasks.add(task);
            given(taskService.getTasks()).willReturn(tasks);

            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(TASK_ID))
                    .andExpect(jsonPath("$[0].title").value(TASK_TITLE))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("POST /tasks")
    class TaskPost {
        @DisplayName("성공시 201 status를 응답한다.")
        @Test
        void create() throws Exception {
            given(taskService.createTask(any())).willReturn(task);

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(taskToString(task))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(TASK_ID))
                    .andExpect(jsonPath("$.title").value(TASK_TITLE));
        }


    }

    @Nested
    @DisplayName("GET /tasks/{id}")
    class TaskGetById {
        @DisplayName("아이디가 존재하면 존재하는 task를 응답한다.")
        @Test
        void existId() throws Exception {
            given(taskService.getTask(TASK_ID)).willReturn(task);

            mockMvc.perform(get("/tasks/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(TASK_TITLE))
                    .andExpect(jsonPath("$.id").value(TASK_ID));
        }
    }

    @Nested
    @DisplayName("PUT /tasks/{id}")
    class TaskPutById {
        @DisplayName("아이디가 존재하면 변경된 task를 응답한다.")
        @Test
        void existId() throws Exception {
            task.setTitle("babo1");
            given(taskService.updateTask(anyLong(), any())).willReturn(task);

            mockMvc.perform(put("/tasks/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(taskToString(task))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("babo1"))
                    .andExpect(jsonPath("$.id").value(TASK_ID))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{id}")
    class TaskDeleteById {
        @DisplayName("아이디가 존재하면 삭제 후 status 204를 응답한다.")
        @Test
        void existId() throws Exception {
            given(taskService.deleteTask(TASK_ID)).willReturn(task);

            mockMvc.perform(delete("/tasks/1"))
                    .andExpect(status().isNoContent());
        }
    }

    private String taskToString(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
