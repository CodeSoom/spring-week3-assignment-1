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

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스")
public class TestControllerTest {
    private static final Long GIVEN_ID = 1L;
    private static final String GIVEN_TITLE = "task1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Nested
    @DisplayName("GET /TASKS는")
    class Describe_getTasks {
        @Nested
        @DisplayName("서비스에 Tasks가 존재하면")
        class Context_with_tasks {
            @BeforeEach
            void setUp() {
                Task task = new Task();
                task.setId(GIVEN_ID);
                task.setTitle(GIVEN_TITLE);
                List<Task> tasks = new ArrayList<>();
                tasks.add(task);

                given(taskService.getTasks())
                        .willReturn(tasks);
            }

            @DisplayName("HTTP Status OK를 리턴한다.")
            @Test
            void it_return_status_ok() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(GIVEN_TITLE)));
            }
        }
    }

}
