package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(TaskController.class)
public class TaskControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        List<Task> tasks = new ArrayList<>();

        Task beforeTask = new Task();
        beforeTask.setTitle("Test task");
        tasks.add(beforeTask);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(1L)).willReturn(beforeTask);
        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
    }

    @Test
    @DisplayName("TaskController 클래스의 list 메소드는 List<Task>를 반환한다")
    void listTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test task")));
    }

    @Test
    @DisplayName("TaskController 클래스의 detail 메소드는 id가 있다면 해당 Task를 반환한다")
    void detailTaskWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test task")));
    }

    @Test
    @DisplayName("TaskController 클래스의 create 메소드는 title을 입력받아 Task를 생성한다")
    void createTask() throws Exception {
        Task task = new Task();
        task.setTitle("Second");
        given(taskService.createTask(any())).willReturn(task);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Second\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("TaskController 클래스의 update 메소드는 id가 있다면 해당 Task를 수정한다")
    void updateTaskWIthValidId() throws Exception {
        mockMvc.perform(patch("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"new\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("TaskController 클래스의 update 메소드는 id가 없다면 NOT_FOUND를 반환한다")
    void updateTaskWIthInvalidId() throws Exception {
        Task updateSource = new Task();
        updateSource.setTitle("new");
        given(taskService.updateTask(100L, updateSource)).willThrow(new TaskNotFoundException(100L));

        mockMvc.perform(patch("/tasks/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\" : null , \"title\" : \"new\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TaskController 클래스의 delete 메소드는 id가 있다면 해당 Task를 삭제한다")
    void deleteTaskWithValidId() throws Exception {
        mockMvc.perform(delete("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Task를 삭제할 때 id가 존재하지 않는다면  NOT_FOUND를 반환한다")
    void deleteTaskWithInvalidId() throws Exception {
        given(taskService.deleteTask(100L)).willThrow(new TaskNotFoundException(100L));

        mockMvc.perform(delete("/tasks/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
