package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskService taskService;


    @DisplayName("list method")
    @Nested
    class Describe_list {

        @DisplayName("if there is nothing in the database ")
        @Nested
        class Context {

            @DisplayName("returns an empty list with a 200 status")
            @Test
            void it_returns_empty_list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"))
                        .andDo(print());
            }

            @DisplayName("returns a list of one task")
            @Test
            void it_returns_one_lists() throws Exception {
                taskService.createTask(new Task(1L, "title"));
                mockMvc.perform(get("/tasks").accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$[0].title",is("title")))
                        .andExpect(jsonPath("$[0].id", is(1)))
                        .andDo(print());
            }
        }
    }


    @Test
    void detailWithValidId() throws Exception {
        //given
        Task task = new Task(1L, "title");
        given(taskService.getTask(1L)).willReturn(task);

        //when
        //then
        mockMvc.perform(get("/tasks/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());
    }

    @Test
    void detailWithInvalidId() throws Exception {

        //given
        given(taskService.getTask(2L)).willThrow(TaskNotFoundException.class);

        //when
        //then
        mockMvc.perform(get("/tasks/2"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void createWithValidId() throws Exception {
        //given
        Task task = new Task(1L, "title");
        given(taskService.generateId()).willReturn(1L);
        given(taskService.createTask(any())).willReturn(task);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .content(objectMapper.writeValueAsString(new Task(1L,"title")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());
    }

    @Test
    void createWithInvalidTask(){
        //given
//        given(taskService.createTask(any())).willReturn();
        //when

        //then
    }

    @Test
    void updateWithValidId() throws Exception {
        //given
        Task updatedTask = new Task(1L, "updated");
        given(taskService.getTask(1L)).willReturn(new Task(1L, "title"));

        given(taskService.updateTask(anyLong(), any())).willReturn(updatedTask);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                        .content(objectMapper.writeValueAsString(new Task(1L,"title")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());
    }
    @Test
    void updateWithInvalidId() throws Exception {
        //given
        given(taskService.updateTask(anyLong(), any())).willThrow(TaskNotFoundException.class);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                        .content(objectMapper.writeValueAsString(new Task(1L,"title")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    void deleteWithValidId() throws Exception {
        //given
        given(taskService.deleteTask(any())).willReturn(Optional.ofNullable(new Task(1L, "title")));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1")
                        .content(objectMapper.writeValueAsString(new Task(1L,"title")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deleteWithInvalidId() throws Exception {
        //given
        given(taskService.deleteTask(anyLong())).willThrow(TaskNotFoundException.class);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
