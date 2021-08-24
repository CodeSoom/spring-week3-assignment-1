package com.codesoom.assignment.models;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TaskTest {
    @Autowired
    TaskService taskService;

    @BeforeEach
    void setUp(){
        Task task = new Task();
        task.setTitle("Test1");
        taskService.createTask(task);
    }

    @Test
    void getTask() throws Exception{
        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isNotNull()
                .isNotBlank()
                .isInstanceOf(String.class);

        assertThat(task.getId()).isNotNull()
                .isInstanceOf(Long.class);
    }
}
