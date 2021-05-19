package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TaskControllerWebTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    List<Task> tasks;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("list() 메소드는")
    class describe_list {
        @Test
        @DisplayName("Response Status가 200이다.")
        void list_response_status() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(1L);
            task.setTitle("First Task");
            tasks.add(task);
            given(taskService.getTasks())
                    .willReturn(tasks);

            //when
            final ResultActions resultActions = mockMvc
                    .perform(get("/tasks")
                            .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("Response MediaType을 application/json이다.")
        void list_response_media_type() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(1L);
            task.setTitle("First Task");
            tasks.add(task);
            given(taskService.getTasks())
                    .willReturn(tasks);

            //when
            final ResultActions resultActions = mockMvc
                    .perform(get("/tasks")
                            .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }

        @Test
        @DisplayName("JSON 결과에 \"First Task\" 을 포함한다.")
        void list_response_contain_json() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(1L);
            task.setTitle("First Task");
            tasks.add(task);
            given(taskService.getTasks())
                    .willReturn(tasks);

            //when
            final ResultActions resultActions = mockMvc
                    .perform(get("/tasks")
                            .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(content().string(containsString("First Task")))
                    .andDo(print());
        }

        @Test
        @DisplayName("JSON 결과가 \"[{\"id\":1,\"title\":\"First Task\"}]\" 과 같다.")
        void list_response_eqaul_json() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(1L);
            task.setTitle("First Task");
            tasks.add(task);
            String jsonTasks = objectMapper.writeValueAsString(tasks);
            given(taskService.getTasks())
                    .willReturn(tasks);

            //when
            final ResultActions resultActions = mockMvc
                    .perform(get("/tasks")
                            .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(content().string(jsonTasks))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("create(Task task) 메소드는")
    class describe_create {
        @Test
        @DisplayName("Response Status가 201이다.")
        void create_response_status() throws Exception {
            //given
            Task task = new Task();
            task.setId(1L);
            task.setTitle("First Task");
            String jsonTask = objectMapper.writeValueAsString(task);

            //when
            final ResultActions resultActions = mockMvc
                    .perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonTask));

            //then
            resultActions
                    .andExpect(status().isCreated())
                    .andDo(print());
        }

        @Test
        @DisplayName(" Task ID가 1이고 Title이 First Task를 생성하면 동일한 Task를 반환한다. ")
        void create_response_task() throws Exception {
            // given
            Task task = new Task();
            task.setId(1L);
            task.setTitle("First Task");
            given(taskService.createTask(any(Task.class)))
                    .willReturn(task);
            String jsonTask = objectMapper.writeValueAsString(task);

            // when
//            when(taskService.createTask(any(Task.class))).thenReturn(task);
            final ResultActions resultActions = mockMvc.perform(post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonTask));

            //then
            resultActions.andExpect(status().isCreated())
                    .andExpect(content().string(containsString(jsonTask)))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("update(Task task) 메소드는")
    class describe_update {
        @Test
        @DisplayName("Response Status 가 200 이다.")
        void update_status() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(1L);
            task.setTitle("First Task");
            String jsonTask = objectMapper.writeValueAsString(task);
            given(taskService.updateTask(eq(1L), any(Task.class)))
                    .willReturn(task);

            //when
            final ResultActions resultActions = mockMvc
                    .perform(put(String.format("/tasks/%d", task.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonTask));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("Task ID가 \"1L\"인 Task의 Title을 \"Sencond Task\"로 수정한다.")
        void update_with_valid() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(1L);
            task.setTitle("Sencond Task");
            String jsonTask = objectMapper.writeValueAsString(task);
            given(taskService.updateTask(eq(1L), any(Task.class)))
                    .willReturn(task);

            //when
            final ResultActions resultActions = mockMvc
                    .perform(put(String.format("/tasks/%d", task.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonTask));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString(jsonTask)))
                    .andDo(print());
        }

        @Test
        @DisplayName("존재하지 않는 Task ID \"2L\"을 수정 하면 Response Status 404이다.")
        void update_task_invalid_status() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(2L);
            task.setTitle("Sencond Task");
            String jsonTask = objectMapper.writeValueAsString(task);
            given(taskService.updateTask(any(), any(Task.class)))
                    .willThrow(new TaskNotFoundException(task.getId()));

            //when
            final ResultActions resultActions = mockMvc
                    .perform(put(String.format("/tasks/%d", task.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonTask));

            //then
            resultActions
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        @DisplayName("존재하지 않는 Task ID \"2L\"을 수정 하면 TaskNotFoundException이 발생한다.")
        void update_task_invalid_exception() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(2L);
            task.setTitle("Sencond Task");
            String jsonTask = objectMapper.writeValueAsString(task);
            given(taskService.updateTask(eq(task.getId()), any()))
                    .willThrow(new TaskNotFoundException(task.getId()));

            //when
            final ResultActions resultActions = mockMvc
                    .perform(put(String.format("/tasks/%d", task.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonTask));

            resultActions.andExpect(
                    result -> {
                        assertTrue(result
                                .getResolvedException()
                                .getClass()
                                .isAssignableFrom(TaskNotFoundException.class));
                    }
            )
                    .andDo(print());
        }
    }



    @Nested
    @DisplayName("patch(Task task) 메소드는")
    class describe_patch{
        @Test
        @DisplayName("Response Status 가 200 이다.")
        void patch_status() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(1L);
            task.setTitle("First Task");
            String jsonTask = objectMapper.writeValueAsString(task);
            given(taskService.updateTask(eq(1L), any(Task.class)))
                    .willReturn(task);

            //when
            final ResultActions resultActions = mockMvc
                    .perform(patch(String.format("/tasks/%d", task.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonTask));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("Task ID가 \"1L\"인 Task의 Title을 \"Sencond Task\"로 수정한다.")
        void patch_with_valid() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(1L);
            task.setTitle("Sencond Task");
            String jsonTask = objectMapper.writeValueAsString(task);
            given(taskService.updateTask(eq(1L), any(Task.class)))
                    .willReturn(task);

            //when
            final ResultActions resultActions = mockMvc
                    .perform(patch(String.format("/tasks/%d", task.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonTask));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString(jsonTask)))
                    .andDo(print());
        }

        @Test
        @DisplayName("존재하지 않는 Task ID \"2L\"을 수정 하면 Response Status 404이다.")
        void patch_task_invalid_status() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(2L);
            task.setTitle("Sencond Task");
            String jsonTask = objectMapper.writeValueAsString(task);
            given(taskService.updateTask(any(), any(Task.class)))
                    .willThrow(new TaskNotFoundException(task.getId()));

            //when
            final ResultActions resultActions = mockMvc
                    .perform(patch(String.format("/tasks/%d", task.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonTask));

            //then
            resultActions
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        @DisplayName("존재하지 않는 Task ID \"2L\"을 수정 하면 TaskNotFoundException이 발생한다.")
        void patch_task_invalid_exception() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(2L);
            task.setTitle("Sencond Task");
            String jsonTask = objectMapper.writeValueAsString(task);
            given(taskService.updateTask(eq(task.getId()), any()))
                    .willThrow(new TaskNotFoundException(task.getId()));

            //when
            final ResultActions resultActions = mockMvc
                    .perform(patch(String.format("/tasks/%d", task.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonTask));

            resultActions.andExpect(
                    result -> {
                        assertTrue(result
                                .getResolvedException()
                                .getClass()
                                .isAssignableFrom(TaskNotFoundException.class));
                    }
            )
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("delete(Long id) 메소드는")
    class describe_delete {
        @Test
        @DisplayName("Response Status 가 204 이다.")
        public void delete_status() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(1L);
            task.setTitle("First Task");
            given(taskService.deleteTask(eq(task.getId())))
                    .willReturn(task);

            //when
            final ResultActions resultActions = mockMvc
                    .perform(delete(String.format("/tasks/%d", task.getId()))
                            .contentType(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(status().isNoContent())
                    .andDo(print());
        }

        @Test
        @DisplayName("존재하는 Task ID \"1L\"을 삭제 하면 응답 값이 없다.")
        public void delete_with_valid() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(1L);
            task.setTitle("First Task");
            given(taskService.deleteTask(eq(task.getId())))
                    .willReturn(task);

            //when
            ResultActions resultActions = mockMvc
                    .perform(delete(String.format("/tasks/%d", task.getId()))
                            .contentType(MediaType.APPLICATION_JSON));

            resultActions
                    .andExpect(content().string(""));
        }

        @Test
        @DisplayName("존재하지 않는 Task ID \"2L\"을 삭제 하면 TaskNotFoundException이 발생한다.")
        public void delete_with_invalid() throws Exception {
            //given
            tasks = new ArrayList<>();
            Task task = new Task();
            task.setId(2L);
            task.setTitle("Second Task");
            given(taskService.deleteTask(eq(task.getId())))
                    .willThrow(new TaskNotFoundException(task.getId()));


            //when
            final ResultActions resultActions = mockMvc
                    .perform(delete(String.format("/tasks/%d", task.getId()))
                            .contentType(MediaType.APPLICATION_JSON));

            //then
            resultActions
                    .andExpect(
                            result -> {
                                result
                                        .getResolvedException()
                                        .getClass()
                                        .isAssignableFrom(TaskNotFoundException.class);
                            }
                    )
                    .andDo(print());
        }
    }
}
