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
    final Long VALID_REQUEST_TASK_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private List<Task> tasks = new ArrayList<>();

    private Task task = new Task();

    @BeforeEach
    void setUp() {
        task.setTitle(TASK_TITLE_ONE);
        tasks.add(task);
    }

    @Nested
    @DisplayName("CREATE")
    class Describe_create {
        private ObjectMapper objectMapper = new ObjectMapper();

        @BeforeEach
        void setUp() {
            task.setTitle(TASK_TITLE_TWO);
        }

        @Test
        @DisplayName("create메소드는 클라이언트가 요청한 새로운 Task를 Tasks에 추가해줍니다.")
        void create() throws Exception {
            given(taskService.createTask(any())).willReturn(task);

            mockMvc.perform(post("/tasks")
                            .content(objectMapper.writeValueAsString(task))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(TASK_TITLE_TWO)))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("READ")
    class Describe_read {
        @Nested
        @DisplayName("list 메소드는")
        class Describe_list {
            @Test
            @DisplayName("Tasks에 있는 모든 Task를 반환합니다.")
            void list() throws Exception {
                given(taskService.getTasks()).willReturn(tasks);

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
                    given(taskService.getTask(VALID_REQUEST_TASK_ID)).willReturn(task);

                    mockMvc.perform(get("/tasks/1"))
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(TASK_TITLE_ONE)));
                }
            }

            @Nested
            @DisplayName("클라이언트가 요청한 Task의 id가 Tasks에 없으면")
            class Context_with_invalid_id {
                final long INVALID_REQUEST_TASK_ID = 100L;

                @Test
                @DisplayName("TaskNotFoundException 예외를 던집니다.")
                void detailWithInvalidId() throws Exception {
                    given(taskService.getTask(INVALID_REQUEST_TASK_ID)).
                            willThrow(new TaskNotFoundException(INVALID_REQUEST_TASK_ID));

                    mockMvc.perform(get("/tasks/" + INVALID_REQUEST_TASK_ID))
                            .andExpect(status().isNotFound());
                }
            }
        }
    }

    @Nested
    @DisplayName("DELETE")
    class Describe_delete {
        @Test
        @DisplayName("delete 메소드는 tsks에서 클라이언트가 요청한 id에 해당하는 Task를 지웁니다.")
        void deleteTask() throws Exception {
            given(taskService.deleteTask(VALID_REQUEST_TASK_ID)).willReturn(null);

            mockMvc.perform(delete("/tasks/{taskId}", VALID_REQUEST_TASK_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

    }

    @Nested
    @DisplayName("UPDATE")
    class Describe_update {
        private static final String UPDATE_TITLE = "otherTest";

        private ObjectMapper objectMapper = new ObjectMapper();

        @Test
        @DisplayName("update 메소드는 tasks에서 클라이언트가 요청한 id에 해당하는 Task의 title을 변경해줍니다.")
        void update() throws Exception {
            task.setTitle(UPDATE_TITLE);

            given(taskService.updateTask(VALID_REQUEST_TASK_ID, task)).willReturn(task);

            mockMvc.perform(put("/tasks/{taskId}", VALID_REQUEST_TASK_ID)
                            .content(objectMapper.writeValueAsString(task))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString(UPDATE_TITLE)))
                    .andDo(print());
        }
    }
}
