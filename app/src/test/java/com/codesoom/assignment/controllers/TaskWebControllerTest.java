package com.codesoom.assignment.controllers;


import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;


    @BeforeEach
    void setup(){
        ArrayList<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setTitle("Test Task");

    }

    @DisplayName("/tasks에 대한 테스트")
    @Nested
    class list {
        @Test
        void list() throws Exception {
            //given
            ArrayList<Task> tasks = new ArrayList<>();
            Task task = new Task();
            task.setTitle("Test task");
            tasks.add(task);
            given(taskService.getTasks()).willReturn(tasks);

            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Test task")));
        }

        @Test
        public void contextEmpty() throws Exception {
            //given
            ArrayList<Task> tasks = new ArrayList<>();
            given(taskService.getTasks()).willReturn(tasks);
            //when
            mockMvc.perform(get("/tasks"))
                    .andExpect(content().string("[]"))
                    .andExpect(status().isOk());
        }

    }

    @DisplayName("/tasks/N에 대한 테스트")
    @Nested
    class getTaskTest {

        @Test
        public void emptyGetIdTask_isOK() throws Exception {
            //given
            Task task = new Task();
            given(taskService.getTask(500L)).willReturn(task);
            //when
            mockMvc.perform(get("/tasks/500"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("{\"id\":null,\"title\":null}"))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        public void detailWithValidId() throws Exception {
            Task task = new Task();
            given(taskService.getTask(1L)).willReturn(task);

            mockMvc.perform(get("/tasks/1"))
                    .andExpect(status().isOk());
        }

        @Test
        public void detailWithInvalidId() throws Exception {
            given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
            mockMvc.perform(get("/tasks/100"))
                    .andExpect(status().isNotFound());
        }

    }

    @DisplayName("POST /tasks ->")
    @Nested
    class CreateTask {
        @Test
        public void createTask() throws Exception {
            //given
            mockMvc.perform(post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            ).andExpect(status().isCreated());
            verify(taskService).createTask(any(Task.class));
        }

    }

    @DisplayName("update")
    @Nested
    class updateProcess {
        @Test
        public void updateTask() throws Exception{
            //given
            mockMvc.perform(patch("/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"title\": \"Rename Task\"}"))
                    .andExpect(status().isOk());
            //when
            verify(taskService).updateTask(eq(1L),any(Task.class));
            //Then
        }

        @Test
        public void updateTaskInValid() throws Exception{
            //given
            mockMvc.perform(patch("/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentType("{\"title\": \"Rename Task\"}"))
                    .andExpect(status().isNoContent());
            //when

            //Then
        }
    }

    @Test
    public void deleteValid() throws Exception {
        //given
        Task task = new Task();

        given(taskService.deleteTask(1L)).willReturn(task);
        //when
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

}