package com.codesoom.assignment.controller;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    @Autowired // 우리가 new를 하지 않고 spring이 자동으로 넣어주는 것
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService; // 실제가 아닌 가짜인데, 진짜처럼 작동하게 해서 테스트함

    List<Task> tasks;
    Task task;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        given(taskService.getTask(1L)).willReturn(task);

        given(taskService.getTask(100L))
                .willThrow(new TaskNotFoundException(100L));
    }

    @AfterEach
    void clear() {
        Mockito.reset(taskService);
    }

    @Nested
    @DisplayName("GET 요청은")
    class Descrivve_GET {
        @Nested
        @DisplayName("할 일 목록에 저장된 데이터가 있으면")
        class Context_with_tasks {
            @BeforeEach
            void setUp() {
                tasks.add(task);
                given(taskService.getTasks()).willReturn(tasks);
            }
            @Test
            @DisplayName("200 응답코드와 저장 되어있는 할 일을 리턴한다.")
            void It_returns_200_and_tasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("Test Task")));
            }
        }

        @Nested
        @DisplayName("할 일 목록에 저장된 데이터가 없으면")
        class Context_with_no_task {
            @BeforeEach
            void setUp() {
                given(taskService.getTasks()).willReturn(tasks);
            }
            @Test
            @DisplayName("200 응답코드와 비어있는 목록을 리턴한다")
            void it_returns_200_and_tasks() throws Exception {

                mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"));

            }
        }
    }

    @Test
    void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());

    }

    @Test
    void detailWithInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

}