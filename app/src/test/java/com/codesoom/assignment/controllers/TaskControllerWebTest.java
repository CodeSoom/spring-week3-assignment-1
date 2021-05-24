package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
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
                source1 = generateTask(1L, "task1");
                source2 = generateTask(2L, "task2");
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

    @Nested
    @DisplayName("'/tasks/:id'에 GET 요청시")
    class Describe_of_GET_tasks_with_id {

        @Nested
        @DisplayName("존재하는 Task의 id를 path에 포함시키면")
        class Context_of_exist_id {

            private Task existTask;
            private long givenId;

            @BeforeEach
            void setup() {
                existTask = generateTask(1L, "task1");
                givenId = existTask.getId();

                given(taskService.getTask(givenId))
                        .willReturn(existTask);
            }

            @Test
            @DisplayName("Task 정보를 JSON으로 응답한다")
            public void getTask() throws Exception {
                mockMvc.perform(get(String.format("/tasks/%d", givenId)))
                        .andExpect(status().isOk())
                        .andExpect(content().json("{'id':1, 'title':'task1'}"));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 Task의 id를 path에 포함시키면")
        class Context_of_non_existent_id {

            private long nonExistentId;

            @BeforeEach
            void setup() {
                nonExistentId = 42L;
                given(taskService.getTask(nonExistentId))
                        .willThrow(new TaskNotFoundException(nonExistentId));
            }

            @Test
            @DisplayName("404 상태코드와 에러메세지를 반환한다")
            public void getTaskWithInvalidId() throws Exception {
                mockMvc.perform(get(String.format("/tasks/%d", nonExistentId)))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message").value("Task not found"));
            }
        }
    }

    @Nested
    @DisplayName("'/tasks'에 POST 요청시")
    class Describe_of_POST_tasks {

        @Nested
        @DisplayName("만약 Task에 관한 정보를 요청 body로 포함시켰고, 생성에 성공했다면")
        class Context_of_success {

            private Task source;
            private String sourceAsJson;

            @BeforeEach
            void setup() throws JsonProcessingException {
                source = generateTask(1L, "task1");

                given(taskService.createTask(source))
                        .willReturn(source);

                ObjectMapper objectMapper = new ObjectMapper();
                sourceAsJson = objectMapper.writeValueAsString(source);
            }

            @Test
            @DisplayName("생성된 Task 정보를 JSON으로 응답한다")
            public void createTask() throws Exception {
                mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(sourceAsJson))
                        .andExpect(status().isCreated())
                        .andExpect(content().json(sourceAsJson));
            }
        }
    }

    @Nested
    @DisplayName("'/tasks/:id'로 PUT 요청시")
    class Describe_of_PUT_tasks_with_id {

        @Nested
        @DisplayName("만약 Task에 관한 정보를 요청 body로 포함시켰고, 업데이트에 성공했다면")
        class Context_of_success {

            private Task source;
            private long givenId;
            private String sourceAsJson;

            @BeforeEach
            void setup() throws JsonProcessingException {
                source = generateTask(1L, "task1");
                givenId = source.getId();

                given(taskService.updateTask(givenId, source))
                        .willReturn(source);

                ObjectMapper objectMapper = new ObjectMapper();
                sourceAsJson = objectMapper.writeValueAsString(source);
            }

            @Test
            @DisplayName("업데이트한 Task 정보를 JSON으로 응답한다")
            public void updateTask() throws Exception {
                mockMvc.perform(put(String.format("/tasks/%d", source.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(sourceAsJson))
                        .andExpect(status().isOk())
                        .andExpect(content().json(sourceAsJson));
            }
        }
    }

    @Nested
    @DisplayName("'/tasks/:id'로 PATCH 요청시")
    class Describe_of_PATCH_tasks_with_id {

        @Nested
        @DisplayName("만약 Task에 관한 정보를 요청 body로 포함시켰고, 업데이트에 성공했다면")
        class Context_of_success {

            private Task source;
            private long givenId;
            private String sourceAsJson;

            @BeforeEach
            void setup() throws JsonProcessingException {
                source = generateTask(1L, "task1");
                givenId = source.getId();

                given(taskService.updateTask(givenId, source))
                        .willReturn(source);

                ObjectMapper objectMapper = new ObjectMapper();
                sourceAsJson = objectMapper.writeValueAsString(source);
            }

            @Test
            @DisplayName("업데이트한 Task 정보를 JSON으로 응답한다")
            public void updateTask() throws Exception {
                mockMvc.perform(put(String.format("/tasks/%d", givenId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(sourceAsJson))
                        .andExpect(status().isOk())
                        .andExpect(content().json(sourceAsJson));
            }
        }
    }

    @Nested
    @DisplayName("'/tasks/:id'로 DELETE 요청시")
    class Describe_of_DELETE_tasks_with_id {

        @Nested
        @DisplayName("존재하는 Task의 id를 path에 포함시켰고, 삭제를 성공했다면")
        class Context_of_exist_id {

            private long givenId;
            private Task givenTask;

            @BeforeEach
            void setup() {
                givenTask = generateTask(1L, "task1");
                givenId = givenTask.getId();

                given(taskService.deleteTask(givenId))
                        .willReturn(null);
            }

            @Test
            @DisplayName("NoContent 상태코드를 응답한다")
            public void it_returns_no_content() throws Exception {
                mockMvc.perform(delete(String.format("/tasks/%d", givenId)))
                        .andExpect(status().isNoContent());
            }
        }
    }
}
