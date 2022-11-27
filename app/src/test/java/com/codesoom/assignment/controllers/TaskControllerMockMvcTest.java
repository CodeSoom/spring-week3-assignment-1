package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.TitleNotFoundException;
import com.codesoom.assignment.application.TaskServiceImpl;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImpl service;

    @BeforeEach
    void before() {
        given(service.getTask(1L))
                .willReturn(new Task(1L, "test"));

        doThrow(TaskNotFoundException.class)
                .when(service)
                .getTask(100L);

        doThrow(TaskNotFoundException.class)
                .when(service)
                .updateTask(eq(100L), any(Task.class));

        doThrow(TaskNotFoundException.class)
                .when(service)
                .deleteTask(100L);
    }

    @Test
    @DisplayName("GET Tasks 목록 테스트")
    void getTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());

        verify(service).getTasks();
    }

    @Test
    @DisplayName("GET 해당 Id에 해당하는 Task 얻고 title이 존재하는지 테스트")
    void getTaskWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());

        verify(service).getTask(1L);
    }

    @Test
    @DisplayName("GET 존재하지 않는 Id로 Task를 찾을 때 TaskNotFoundException을 던지는지 테스트")
    void getTaskWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                                        .isInstanceOf(TaskNotFoundException.class));

        verify(service).getTask(100L);
    }

    @Test
    @DisplayName("POST Task 객체 생성 테스트")
    void  createTaskWithTitle() throws Exception {
        //1. ObjectMapper를 활용하여 Content 만드는 방법
        Task task = new Task(2L, "test2");
        String content = new ObjectMapper().writeValueAsString(task);

        //perform() 안에 조건 집어넣기
        mockMvc.perform(post("/tasks")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(service).createTask(any(Task.class));
    }

    @Test
    @DisplayName("POST empty title일 경우 TitleNotFoundException 테스트")
    void  createTaskWithoutTitle() throws Exception {
        mockMvc.perform(post("/tasks")
                        .content("{\"title\":\"\"}")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(TitleNotFoundException.class));

        //TODO verify 어떻게?
    }

    @Test
    @DisplayName("PUT title을 test -> test2 수정 테스트")
    void putTask() throws Exception {
        mockMvc.perform(put("/tasks/1")
                        .content("{\"title\":\"test2\"}") //2. 직접 JSON을 넣는 방법
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(service).updateTask(eq(1L), any(Task.class));
    }

    @Test
    @DisplayName("PATCH title을 test -> test3 수정 테스트")
    void patchTask() throws Exception {
        mockMvc.perform(patch("/tasks/1")
                        .content("{\"title\":\"test3\"}")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service).updateTask(eq(1L), any(Task.class));
    }

    @Test
    @DisplayName("PUT 존재하지 않은 Task로 updateTask를 시도할 경우 테스트")
    void putTaskNotExistedTask() throws Exception {
        mockMvc.perform(patch("/tasks/100")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"존재하지 않는 Task\"}")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).updateTask(eq(100L), any(Task.class));
    }

    @Test
    @DisplayName("PATCH 존재하지 않은 Task로 updateTask를 시도할 경우 테스트")
    void patchTaskNotExistedTask() throws Exception {
        mockMvc.perform(patch("/tasks/100")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"존재하지 않는 Task\"}")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).updateTask(eq(100L), any(Task.class));
    }

    @Test
    @DisplayName("DELETE id가 1번인 Task 삭제 후 NO_CONTENT 리턴 테스트")
    void deleteTaskWithValidId() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        verify(service).deleteTask(1L);
    }

    @Test
    @DisplayName("DELETE id가 100번인 Task 삭제 후 NO_CONTENT 리턴 테스트")
    void deleteTaskWithInvalidId() throws Exception {
        mockMvc.perform(delete("/tasks/100"))
                .andExpect(status().isNotFound());

        verify(service).deleteTask(100L);
    }
}
