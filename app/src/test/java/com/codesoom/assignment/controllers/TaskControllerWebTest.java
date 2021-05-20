package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스 Web")
public class TaskControllerWebTest {

    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    private Task generateTask(long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        return task;
    }

    @Nested
    @DisplayName("'/tasks'에 GET 요청시")
    class Describe_of_GET_tasks {

        @Nested
        @DisplayName("만약 tasks가 비어있다면")
        class Context_of_empty_tasks {

            @BeforeEach
            void setup() {
                List<Task> emptyList = Collections.<Task>emptyList();
                given(taskService.getTasks()).willReturn(emptyList);
            }

            @Test
            @DisplayName("비어있는 배열을 응답한다")
            public void it_returns_empty_array() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"));
            }
        }

        @Nested
        @DisplayName("만약 tasks가 비어있지 않다면")
        class Context_of_not_empty_tasks {

            private Task source1;
            private Task source2;

            @BeforeEach
            void setup() {
                this.source1 = generateTask(1L, "task1");
                this.source2 = generateTask(2L, "task2");
                List<Task> taskList = List.of(source1, source2);

                given(taskService.getTasks()).willReturn(taskList);
            }

            @Test
            @DisplayName("모든 tasks를 JSON 배열로 응답한다")
            public void it_returns_all_tasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$[0]").value(source1))
                        .andExpect(jsonPath("$[1]").value(source2));
            }
        }
    }

    @Test
    public void getTask() throws Exception {
        // given
        Task task = new Task();

        task.setId(1L);
        task.setTitle("task1");
        given(taskService.getTask(task.getId()))
                .willReturn(task);

        // when
        mockMvc.perform(get(String.format("/tasks/%d", task.getId())))
        // then
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1, 'title':'task1'}"));

    }

    @Test
    public void getTaskWithInvalidId() throws Exception {
        // given
        long invalidId = 42L;
        given(taskService.getTask(invalidId))
                .willThrow(new TaskNotFoundException(invalidId));

        // when
        mockMvc.perform(get(String.format("/tasks/%d", invalidId)))
        // then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }

    @Test
    public void createTask() throws Exception {
        // given
        Task source = new Task();
        source.setId(1L);
        source.setTitle("task1");
        given(taskService.createTask(source))
                .willReturn(source);
        ObjectMapper objectMapper = new ObjectMapper();
        String sourceJson = objectMapper.writeValueAsString(source);

        // when
        mockMvc.perform(post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(sourceJson))
        // then
                .andExpect(status().isCreated())
                .andExpect(content().json(sourceJson));
    }

    @Test
    public void updateTask() throws Exception {
        // given
        Task source = new Task();
        source.setId(1L);
        source.setTitle("task1");
        given(taskService.updateTask(source.getId(), source))
                .willReturn(source);
        ObjectMapper objectMapper = new ObjectMapper();
        String sourceJson = objectMapper.writeValueAsString(source);

        // when
        mockMvc.perform(put(String.format("/tasks/%d", source.getId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(sourceJson))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(sourceJson));

    }

    @Test
    public void patchTask() throws Exception {
        // given
        Task source = new Task();
        source.setId(1L);
        source.setTitle("task1");
        given(taskService.updateTask(source.getId(), source))
                .willReturn(source);
        ObjectMapper objectMapper = new ObjectMapper();
        String sourceJson = objectMapper.writeValueAsString(source);

        // when
        mockMvc.perform(patch(String.format("/tasks/%d", source.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(sourceJson))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(sourceJson));

    }

    @Test
    public void deleteTask() throws Exception {
        // given
        given(taskService.deleteTask(1L))
                .willReturn(null);

        // when
        mockMvc.perform(delete(String.format("/tasks/%d", 1L)))
                // then
                .andExpect(status().isNoContent());

    }
}
