package com.codesoom.assignment.controllers;

import com.codesoom.assignment.BaseTaskTest;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerMockTest extends BaseTaskTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private List<Task> tasks;
    private Task task;

    @BeforeEach
    void setUp(){
        tasks = new ArrayList<>();
        task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE_1);
    }

    @AfterEach
    void clear(){
        Mockito.reset(taskService);
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Nested
    @DisplayName("GET /tasks API는")
    class Describe_GET_tasks {
        @Nested
        @DisplayName("등록된 태스크가 없다면")
        class Context_NonExistingTask{
            @Test
            @DisplayName("200 리스폰스와 빈 리스트를 리턴한다")
            void it_returns_empty_list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(objectMapper.writeValueAsString(tasks)));
            }
        }

        @Nested
        @DisplayName("등록된 태스크가 있다면")
        class Context_ExistingTask{
            @BeforeEach
            void setUp(){
                tasks.add(task);
                tasks.add(task);
                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("200 리스폰스와 모든 태스크를 포함한 리스트를 리턴한다")
            void it_returns_task_list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(objectMapper.writeValueAsString(tasks)));
            }
        }
    }
}
