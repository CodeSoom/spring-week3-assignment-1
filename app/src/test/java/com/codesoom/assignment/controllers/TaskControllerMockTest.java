package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.TaskRequestDto;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("할 일 Controller MockMVC 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerMockTest {

    private static final String TITLE = "test";
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TaskService taskService;

    private String toJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("모든 할 일을 조회할 수 있다. 200 응답 코드를 생성한다")
    void list() throws Exception {
        when(taskService.getTasks())
                .thenReturn(List.of(new Task(1L, TITLE), new Task(2L, TITLE)));

        mvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value(TITLE))
                .andDo(print());
    }

    @Test
    @DisplayName("id에 해당하는 할 일을 조회할 수 있다. 200 응답 코드를 생성한다")
    void detail() throws Exception {
        when(taskService.getTask(1L))
                .thenReturn(new Task(1L, TITLE));

        mvc.perform(get("/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value(TITLE))
                .andDo(print());
    }

    @Test
    @DisplayName("id에 해당하는 할 일이 없으면 404 응답 코드를 생성한다")
    void detailWithInvalidId() throws Exception {
        when(taskService.getTask(1000L))
                .thenThrow(TaskNotFoundException.class);

        mvc.perform(get("/tasks/{id}", 1000L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("할 일을 새로 생성할 수 있다. 201 응답 코드를 생성한다")
    void createTask() throws Exception {
        TaskRequestDto source = new TaskRequestDto(TITLE);
        when(taskService.createTask(source))
                .thenReturn(new Task(1L, TITLE));

        mvc.perform(post("/tasks")
                .content(toJsonString(source))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("기존의 할 일을 수정할 수 있다. 200 응답 코드를 생성한다")
    void updateTask() throws Exception {
        Long id = 1L;

        TaskRequestDto source = new TaskRequestDto("update" + TITLE);
        Task task = new Task(id, TITLE);
        Task updatedTask = new Task(id, "update" + TITLE);

        when(taskService.getTask(id)).thenReturn(task);
        when(taskService.updateTask(id, source))
                .thenReturn(updatedTask);

        mvc.perform(put("/tasks/{id}", 1L).content(toJsonString(source))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("update" + TITLE))
                .andDo(print());
    }

    @Test
    @DisplayName("할 일을 삭제할 수 있다. 204 응답 코드를 생성한다")
    void deleteTask() throws Exception {
        when(taskService.deleteTask(1L))
                .thenReturn(new Task(1L, TITLE));

        mvc.perform(delete("/tasks/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
