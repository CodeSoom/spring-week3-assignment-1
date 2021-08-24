package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TaskServiceTest {
    @Autowired
    TaskService taskService;
    private String TITLE = "Test1";
    @BeforeEach
    void setUp(){
        Task source = new Task();
        source.setTitle("Test1");
        taskService.createTask(source);
    }
    @Test
    void getTasks(){
        assertThat(taskService.getTasks()).hasSizeGreaterThan(0);
        assertThat(taskService.getTasks()).hasSize(1);
        assertThat(taskService.getTasks().get(0).getId()).isEqualTo(1L);
    }
    @Test
    void getTask(){
        assertThat(taskService.getTask(1L)).isNotNull();
        assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TITLE);
    }
}
