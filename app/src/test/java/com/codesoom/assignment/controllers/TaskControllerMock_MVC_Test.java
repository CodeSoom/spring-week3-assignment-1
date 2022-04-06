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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerMock_MVC_Test {
    private static final String TASK_TITLE_ONE = "testOne";
    private static final String TASK_TITLE_TWO = "testTwo";
    private static final String UPDATE_TITLE = "otherTest";
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Nested
    @DisplayName("CREATE")
    class Describe_create {
        ObjectMapper objectMapper = new ObjectMapper();

        @Test
        @DisplayName("create메소드는 클라이언트가 요청한 새로운 Task를 Tasks에 추가해줍니다.")
        void create() throws Exception {
            Task task = new Task();
            task.setTitle(TASK_TITLE_ONE);

            given(taskService.createTask(any())).willReturn(task);

            mockMvc.perform(post("/tasks")
                            .content(objectMapper.writeValueAsString(task))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isCreated())
                            .andExpect(content().string(containsString(TASK_TITLE_ONE)))
                            .andDo(print());
        }
    }

    @Nested
    @DisplayName("READ")
    class Describe_read {
        @BeforeEach
        void setUp() {
            List<Task> tasks = new ArrayList<>();

            Task task = new Task();
            task.setTitle(TASK_TITLE_ONE);
            tasks.add(task);

            given(taskService.getTasks()).willReturn(tasks);

            given(taskService.getTask(1L)).willReturn(task);
        }

        @Nested
        @DisplayName("list 메소드는")
        class Describe_list {
            @Test
            @DisplayName("Tasks에 있는 모든 Task를 반환합니다.")
            void list() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TASK_TITLE_ONE)));
            }
        }

        @Nested
        @DisplayName("detail 메소드는")
        class Describe_detail {
            @Nested
            @DisplayName("클라이언트가 요청한 Task의 id가 Tasks에 있으면")
            class Context_with_valid_id {
                @Test
                @DisplayName("id에 해당하는 Task를 반환합니다.")
                void detailWithValidId() throws Exception {
                    mockMvc.perform(get("/tasks/1"))
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(TASK_TITLE_ONE)));
                }
            }

            @Nested
            @DisplayName("클라이언트가 요청한 Task의 id가 Tasks에 없으면")
            class Context_with_invalid_id {
                @Test
                @DisplayName("TaskNotFoundException 예외를 던집니다.")
                void detailWithInvalidId() throws Exception {
                    given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));

                    mockMvc.perform(get("/tasks/100"))
                            .andExpect(status().isNotFound());
                }
            }

        }
    }

    @Nested
    @DisplayName("UPDATE")
    class Describe_update {
        ObjectMapper objectMapper = new ObjectMapper();

        @Test
        @DisplayName("update 메소드는 tasks에서 클라이언트가 요청한 id에 해당하는 Task의 title을 변경해줍니다.")
        void update() throws Exception {
            List<Task> tasks = new ArrayList<>();

            Task task = new Task();
            task.setTitle(TASK_TITLE_ONE);
            tasks.add(task);

            task.setTitle(UPDATE_TITLE);

            given(taskService.updateTask(1L, task)).willReturn(task);

            mockMvc.perform(put("/tasks/{taskId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(task)))
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(UPDATE_TITLE)))
                            .andDo(print());
        }
    }

    @Nested
    @DisplayName("DELETE")
    class Describe_delete {
        @Test
        @DisplayName("delete 메소드는 tsks에서 클라이언트가 요청한 id에 해당하는 Task를 지웁니다.")
        void deleteTask() throws Exception {
            List<Task> tasks = new ArrayList<>();

            Task task = new Task();
            task.setTitle(TASK_TITLE_ONE);
            tasks.add(task);

            given(taskService.deleteTask(1L)).willReturn(null);

            mockMvc.perform(delete("/tasks/{taskId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

    }
}
