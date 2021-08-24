package com.codesoom.assignment.application;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskServiceMockBean {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TaskService taskService;

    @Test
    void getTaskAsMockBean() throws Exception{
        Task task = new Task();
        given(taskService.getTask(1L)).willReturn(task);
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());
    }
}
