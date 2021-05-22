package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

    Task makingTask(Long taskId) {
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Task Title" + taskId);
        return task;
    }

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
    }

    @Nested
    @DisplayName("list() 메소드는")
    class describe_list {
        @Nested
        @DisplayName("정상적으로 list가 반환 됬을 경우")
        class Context_valid_list {
            @Test
            @DisplayName("Response Status가  200이다.")
            void list_response_status() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
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
                Long taskId = 1L;
                Task task = makingTask(taskId);
                tasks.add(task);
                given(taskService.getTasks())
                        .willReturn(tasks);

                //when
                final ResultActions resultActions = mockMvc
                        .perform(get("/tasks")
                                .contentType(MediaType.APPLICATION_JSON));

                //then
                resultActions
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
            }

            @Test
            @DisplayName("JSON 결과에 기존에 생성된 Title 내용을 포함한다.")
            void list_response_contain_json() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                tasks.add(task);
                given(taskService.getTasks())
                        .willReturn(tasks);

                //when
                final ResultActions resultActions = mockMvc
                        .perform(get("/tasks")
                                .accept(MediaType.APPLICATION_JSON));

                //then
                resultActions
                        .andExpect(content().string(containsString(task.getTitle())))
                        .andDo(print());
            }

            @Test
            @DisplayName("값이 있으면 결과를 JSON으로 준다.")
            void list_response_eqaul_json() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
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
    }



    @Nested
    @DisplayName("detail(Long id) 메소드는")
    class Describe_detail{
        @Nested
        @DisplayName("요청한 Task가 존재한다면")
        class Context_valid_detail{

            @Test
            @DisplayName("Response Status가 200이다.")
            void details_response_status() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                given(taskService.getTask(eq(taskId)))
                        .willReturn(task);

                //when
                ResultActions resultActions
                        = mockMvc.perform(get(String.format("/tasks/%d", taskId)).contentType(MediaType.APPLICATION_JSON));

                //then
                resultActions.andExpect(status().isOk());
            }

            @Test
            @DisplayName("요청한 Task를 반환한다")
            void detiail_valid_get_task_by_id() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                given(taskService.getTask(eq(taskId)))
                        .willReturn(task);
                String taskJSON = objectMapper.writeValueAsString(task);

                //when
                ResultActions resultActions = mockMvc
                        .perform(get(String.format("/tasks/%d", taskId))
                                .contentType(MediaType.APPLICATION_JSON));

                //then
                resultActions.andExpect(content().string(containsString(taskJSON)))
                        .andDo(print());
            }

            @Test
            @DisplayName("Response MediaType은 application/json 이다.")
            void details_response_media_type() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                given(taskService.getTask(eq(taskId)))
                        .willReturn(task);
                String taskJSON = objectMapper.writeValueAsString(task);

                //when
                ResultActions resultActions = mockMvc
                        .perform(get(String.format("/tasks/%d", taskId))
                                .contentType(MediaType.APPLICATION_JSON));

                //then
                resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("요청한 Task가 목록에 없다면")
        class Context_invalid_detail{
            @Test
            @DisplayName("Response Status 404이다.")
            void detail_task_invalid_status() throws Exception {
                //given
                Long taskId = 2L;
                Task task = makingTask(taskId);
                String taskJSON = objectMapper.writeValueAsString(task);
                given(taskService.getTask(eq(taskId)))
                        .willThrow(new TaskNotFoundException(task.getId()));

                //when
                ResultActions resultActions = mockMvc
                        .perform(get(String.format(String.format("/tasks/%d",taskId)))
                                .contentType(MediaType.APPLICATION_JSON));

                //then
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

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void detail_task_invalid_exception() throws Exception {
                //given
                Long taskId = 2L;
                Task task = makingTask(taskId);
                String taskJSON = objectMapper.writeValueAsString(task);
                given(taskService.getTask(eq(task.getId())))
                        .willThrow(new TaskNotFoundException(task.getId()));

                //when
                ResultActions resultActions = mockMvc
                        .perform(get(String.format(String.format("/tasks/%d", task.getId())))
                                .contentType(MediaType.APPLICATION_JSON));

                //then
                resultActions
                        .andExpect(status().isNotFound())
                        .andDo(print());
            }
        }

    }

    @Nested
    @DisplayName("create(Task task) 메소드는")
    class describe_create {
        @Nested
        @DisplayName("Task가 정상적으로 등록 되면 ")
        class Context_valid_create_task {
            @Test
            @DisplayName("Response Status가 201이다.")
            void create_response_status() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
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
            @DisplayName("요청한 Task와 동일한 Task를 반환한다. ")
            void create_response_task() throws Exception {
                // given
                Long taskId = 1L;
                Task task = makingTask(taskId);
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
    }

    @Nested
    @DisplayName("update(Task task) 메소드는")
    class describe_update {
        @Nested
        @DisplayName("목록에 존재하는 Task를 수정한다면")
        class Context_valid_update_task {
            @Test
            @DisplayName("Response Status 가 200 이다.")
            void update_valid_status() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                String jsonTask = objectMapper.writeValueAsString(task);
                given(taskService.updateTask(eq(taskId), any(Task.class)))
                        .willReturn(task);

                //when
                final ResultActions resultActions = mockMvc
                        .perform(put(String.format("/tasks/%d", taskId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTask));

                //then
                resultActions
                        .andExpect(status().isOk())
                        .andDo(print());
            }

            @Test
            @DisplayName("목록의 Task 정보를 수정한다.")
            void update_with_valid() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                String jsonTask = objectMapper.writeValueAsString(task);
                given(taskService.updateTask(eq(taskId), any(Task.class)))
                        .willReturn(task);

                //when
                final ResultActions resultActions = mockMvc
                        .perform(put(String.format("/tasks/%d", taskId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTask));

                //then
                resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(jsonTask)))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("요청한 Task가 목록에 없다면")
        class Context_invalid_update_task {
            @Test
            @DisplayName("Response Status 404이다.")
            void update_task_invalid_status() throws Exception {
                //given
                Long taskId = 2L;
                Task task = makingTask(taskId);
                String jsonTask = objectMapper.writeValueAsString(task);
                given(taskService.updateTask(any(), any(Task.class)))
                        .willThrow(new TaskNotFoundException(task.getId()));

                //when
                final ResultActions resultActions = mockMvc
                        .perform(put(String.format("/tasks/%d", taskId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTask));

                //then
                resultActions
                        .andExpect(status().isNotFound())
                        .andDo(print());
            }

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void update_task_invalid_exception() throws Exception {
                //given
                Long taskId = 2L;
                Task task = makingTask(taskId);
                String jsonTask = objectMapper.writeValueAsString(task);
                given(taskService.updateTask(eq(task.getId()), any()))
                        .willThrow(new TaskNotFoundException(task.getId()));

                //when
                final ResultActions resultActions = mockMvc
                        .perform(put(String.format("/tasks/%d", taskId))
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
    }


    @Nested
    @DisplayName("patch(Task task) 메소드는")
    class describe_patch {
        @Nested
        @DisplayName("목록에 존재하는 Task를 수정한다면")
        class Context_valid_patch_task {
            @Test
            @DisplayName("Response Status 가 200 이다.")
            void patch_valid_status() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                String jsonTask = objectMapper.writeValueAsString(task);
                given(taskService.updateTask(eq(taskId), any(Task.class)))
                        .willReturn(task);

                //when
                final ResultActions resultActions = mockMvc
                        .perform(patch(String.format("/tasks/%d", taskId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTask));

                //then
                resultActions
                        .andExpect(status().isOk())
                        .andDo(print());
            }

            @Test
            @DisplayName("목록의 Task 정보를 수정한다.")
            void patch_with_valid() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                String jsonTask = objectMapper.writeValueAsString(task);
                given(taskService.updateTask(eq(taskId), any(Task.class)))
                        .willReturn(task);

                //when
                final ResultActions resultActions = mockMvc
                        .perform(patch(String.format("/tasks/%d", taskId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTask));

                //then
                resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(jsonTask)))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("요청한 Task가 목록에 없다면")
        class Context_invalid_patch_task {
            @Test
            @DisplayName("Response Status 404이다.")
            void patch_task_invalid_status() throws Exception {
                //given
                Long taskId = 2L;
                Task task = makingTask(taskId);
                String jsonTask = objectMapper.writeValueAsString(task);
                given(taskService.updateTask(any(), any(Task.class)))
                        .willThrow(new TaskNotFoundException(task.getId()));

                //when
                final ResultActions resultActions = mockMvc
                        .perform(patch(String.format("/tasks/%d", taskId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTask));

                //then
                resultActions
                        .andExpect(status().isNotFound())
                        .andDo(print());
            }

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void patch_task_invalid_exception() throws Exception {
                //given
                Long taskId = 2L;
                Task task = makingTask(taskId);
                String jsonTask = objectMapper.writeValueAsString(task);
                given(taskService.updateTask(eq(task.getId()), any()))
                        .willThrow(new TaskNotFoundException(task.getId()));

                //when
                final ResultActions resultActions = mockMvc
                        .perform(patch(String.format("/tasks/%d", taskId))
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
    }

    @Nested
    @DisplayName("delete(Long id) 메소드는")
    class describe_delete {

        @Nested
        @DisplayName("요청한 Task가 목록에 있다면")
        class Context_valid_delete {
            @Test
            @DisplayName("Response Status 가 204 이다.")
            public void delete_status() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                given(taskService.deleteTask(eq(taskId)))
                        .willReturn(task);

                //when
                final ResultActions resultActions = mockMvc
                        .perform(delete(String.format("/tasks/%d", taskId))
                                .contentType(MediaType.APPLICATION_JSON));

                //then
                resultActions
                        .andExpect(status().isNoContent())
                        .andDo(print());
            }

            @Test
            @DisplayName("응답 값이 없다.")
            public void delete_with_valid() throws Exception {
                //given
                Long taskId = 1L;
                Task task = makingTask(taskId);
                given(taskService.deleteTask(eq(taskId)))
                        .willReturn(null);

                //when
                ResultActions resultActions = mockMvc
                        .perform(delete(String.format("/tasks/%d", taskId))
                                .contentType(MediaType.APPLICATION_JSON));

                resultActions
                        .andExpect(content().string(""));
            }
        }

        @Nested
        @DisplayName("요청한 Task가 목록에 없다면")
        class Context_valid_delete_task {
            @Test
            @DisplayName("Response Status가 404 이다.")
            void delete_with_invalid_status() throws Exception {
                //given
                Long taskId = 2L;
                Task task = makingTask(taskId);
                given(taskService.deleteTask(eq(taskId)))
                        .willThrow(new TaskNotFoundException(task.getId()));

                //when
                final ResultActions resultActions = mockMvc
                        .perform(delete(String.format("/tasks/%d", taskId))
                        .contentType(MediaType.APPLICATION_JSON));

                //then
                resultActions
                        .andExpect(status().isNotFound())
                        .andDo(print());
            }

            @Test
            @DisplayName("TaskNotFoundException이 발생한다.")
            void delete_with_invalid_exception() throws Exception {
                //given
                Long taskId = 2L;
                Task task = makingTask(taskId);
                given(taskService.deleteTask(eq(taskId)))
                        .willThrow(new TaskNotFoundException(task.getId()));


                //when
                final ResultActions resultActions = mockMvc
                        .perform(delete(String.format("/tasks/%d", taskId))
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
}
