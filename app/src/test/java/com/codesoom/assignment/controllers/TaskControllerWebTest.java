package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Tpdp list와 Detail에 대해서만 Test를 진행했는데 CRUD 전부 테스트 코드를 작성할 것.

@SpringBootTest //통합테스트
@AutoConfigureMockMvc
//@WebMvcTest // 전체적인 것(spring)을 가져오지 못함.
public class TaskControllerWebTest {
    @Autowired
    private ObjectMapper objectMapper;

    // 여기서는 Junit이 아닌 스프링에서 객체를 넣어준다. 기존 TaskControllerTest는 Junit에서만 실행.
    @Autowired
    private MockMvc mockMvc;

    /* 스프링에서는 싱글톤으로 객체를 다 같이 돌려서 쓴다. 이를 해결하기 위한
    // 대안 1
    @BeforeEach
    void setUp() {
        taskService.reset();
    }*/
    // 대안 2
    //스프링에서는 mockito를 직접 사용하지 않고 한번 더 감싸서 MockBean으로 사용.
    @MockBean
    private TaskService taskService;


    @BeforeEach
    void setUp() {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTitle("Test Task");
        tasks.add(task);

        // 가짜인 taskService(Mock)을 진짜인 것처럼 사용하기 위해 given()
        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(1L)).willReturn(task);

        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(containsString("Test Task")));
    }

    @Test
    void detailWithValidId() throws Exception {
        Task task = new Task();
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Task")));
    }

    @Test
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Task task = new Task();
        task.setTitle("글 등록");
        String content = objectMapper.writeValueAsString(task);

        when(taskService.createTask(task)).thenReturn(task);

        mockMvc.perform(post("/tasks")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void update() throws Exception{
        //Mockito.when(patientRecordRepository.save(record)).thenReturn(record);
        Task task = new Task();
        task.setTitle("글 등록");
        String content = objectMapper.writeValueAsString(task);

        mockMvc.perform(put("/tasks/{id}" , 1L)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
                // todo 본문(TaskController) 수정 후에 추가 테스트 필요. response부분 수정 필요. response 내용이 print되지 않는다.
                //.andExpect(jsonPath("$", notNullValue()))
                //.andExpect(jsonPath("$.title", is("글 등록")));
    }

    //Update
    @Test
    void delete() throws Exception{
        /*mockMvc.perform(delete("/tasks/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());*/
    }
}
