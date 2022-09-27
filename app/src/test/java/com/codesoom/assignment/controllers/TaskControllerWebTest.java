package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskController taskController;


    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks")).andExpect(status().isOk());
    }

    @Test
    void detailWithValidId() throws Exception {
        //given
        Task task = new Task(1L, "title");
        given(taskService.getTask(1L)).willReturn(task);
        taskService.createTask(task);

        //when
        ResultActions getTask = mockMvc
                .perform(get("/tasks/1").content(objectMapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON));
        //then
        getTask.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void detailWithInvalidId() throws Exception {

        //given
        Task task = new Task(1L, "title");
        given(taskService.getTask(2L)).willThrow(TaskNotFoundException.class);
        taskService.createTask(task);

        //when
        ResultActions getTask = mockMvc.perform(get("/tasks/2"));

        //then
        getTask.andExpect(status().isNotFound());
    }

//    @Test
//    void createWithValidId() throws Exception {
//        //given
//        Task task = new Task(1L, "title");
//        given(taskService.generateId()).willReturn(1L);
//        given(createTask(any())).willReturn(new Task(1L,"title"));
//        //when
//        //then
//        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")).andDo(print());
////        mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON))
////                .andExpect(jsonPath("$.title").value("title"))
////                .andExpect(jsonPath("$.id").value(1L));
//    }
}
