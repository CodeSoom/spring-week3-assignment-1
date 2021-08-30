package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskControllerWebTest {
    private String TITLE = "Test1";
    private String NEW_TITLE = "Test2";
    private String UPDATED_TITLE = "Test1_updated";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskService taskService;

    @BeforeEach
    void setUp(){
        Task source = new Task();
        source.setTitle(TITLE);
        taskService.createTask(source);
    }

    @Test
    //@Order(3)
    @DisplayName("할일 목록 가져오기")
    void list() throws Exception{
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andDo(print());
    }

    @Test
    //@Order(2)
    @DisplayName("1번 할일 가져오기 Validation : isOk, expect TITLE")
    void getTask() throws Exception{
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TITLE)));
    }

    @Test
    //@Order(1)
    @DisplayName("할일 생성하기  Validation : isCreated, expect NEW_TITLE")
    void createTask() throws Exception{
        Task newTask = new Task();
        newTask.setTitle(NEW_TITLE);
        String content = objectMapper.writeValueAsString(newTask);
        mockMvc.perform(post("/tasks").content(content)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(NEW_TITLE));
    }

    @Test
    //@Order(4)
    @DisplayName("할일 수정하기 Validation : isOk, expect UPDATED_TITLE")
    void updateTask() throws Exception{
        Task newTask = new Task();
        newTask.setTitle(UPDATED_TITLE);

        String content = objectMapper.writeValueAsString(newTask);
        mockMvc.perform(put("/tasks/1").content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(UPDATED_TITLE));
    }

    @Test
    //@Order(5)
    @DisplayName("할일 삭제하기 Validation : isNoContent")
    void deleteTask() throws Exception{
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
