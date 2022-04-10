package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스")
class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final Long TASK_ID = 1L;
    private final String TASK_TITLE = "keep going";

    @Nested
    @DisplayName("할 일 목록 요청은")
    class Describe_list {

        @Nested
        @DisplayName("목록이 있으면")
        class Context_with_tasks{

            @BeforeEach
            void setUp() {
                List<Task> tasks = new ArrayList<>();
                Task task = new Task();

                task.setId(TASK_ID);
                task.setTitle(TASK_TITLE);

                tasks.add(task);

                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("할 일 목록을 반환합니다")
            void it_returns_tasks() throws Exception {
                mockMvc.perform(get("/tasks")).andExpect(status().isOk());
            }
        }
    }
}
