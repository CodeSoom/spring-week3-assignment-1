package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
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

    MockMvc mockMvc;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new TaskController(taskService))
                .setControllerAdvice(TaskErrorAdvice.class)
                .build();
    }

    @Nested
    @DisplayName("CREATE")
    class Describe_create {
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
            objectMapper = new ObjectMapper();
        }

        @Test
        @DisplayName("create메소드는 클라이언트가 요청한 새로운 Task를 Tasks에 추가해줍니다.")
        void create() throws Exception {
            assertThat(taskService.getTasks()).hasSize(0);

            Task task = new Task(TASK_TITLE_ONE);

            mockMvc.perform(post("/tasks")
                            .content(objectMapper.writeValueAsString(task))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(TASK_TITLE_ONE)))
                    .andDo(print());

            assertThat(taskService.getTasks()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("READ")
    class Describe_read {
        @Nested
        @DisplayName("list 메소드는")
        class Describe_list {
            @Nested
            @DisplayName("처음에는")
            class Context_at_first {
                final static String EMPTY_LIST = "[]";
                @Test
                @DisplayName("비어있는 Task 목록을 가지고 있습니다.")
                void list_with_empty () throws Exception {
                    mockMvc.perform(get("/tasks"))
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(EMPTY_LIST)));
                }
            }
            @Nested
            @DisplayName("할 일 목록을 채운 다음에는")
            class Context_after_adding_tasks {
                final static int TASKS_SIZE = 10;
                final static String TASK_TITLE = "test";

                @BeforeEach
                void setUp() {
                    Task task = new Task(TASK_TITLE);
                    for (int i = 0; i < TASKS_SIZE; i++) {
                        task.setTitle(TASK_TITLE + (i + 1));
                        taskService.createTask(task);
                    }
                }

                @Test
                @DisplayName("할 일 목록에 있는 모든 Task들을 반환합니다.")
                void list() throws Exception {
                    mockMvc.perform(get("/tasks"))
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(TASK_TITLE + 1)))
                            .andExpect(content().string(containsString(TASK_TITLE + 2)))
                            .andExpect(content().string(containsString(TASK_TITLE + 3)))
                            .andExpect(content().string(containsString(TASK_TITLE + 4)))
                            .andExpect(content().string(containsString(TASK_TITLE + 5)))
                            .andExpect(content().string(containsString(TASK_TITLE + 6)))
                            .andExpect(content().string(containsString(TASK_TITLE + 7)))
                            .andExpect(content().string(containsString(TASK_TITLE + 8)))
                            .andExpect(content().string(containsString(TASK_TITLE + 9)))
                            .andExpect(content().string(containsString(TASK_TITLE + 10)));
                }
            }
        }

        @Nested
        @DisplayName("detail 메소드는")
        class Describe_detail {
            @Nested
            @DisplayName("클라이언트가 요청한 Task의 id가 Tasks에 있으면")
            class Context_with_valid_id {
                @BeforeEach
                void setUp() {
                    Task task = new Task(TASK_TITLE_ONE);

                    taskService.createTask(task);
                }

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
                final long INVALID_REQUEST_TASK_ID = 11L;
                final static int TASKS_MAX_SIZE = 10;
                final static String TASK_DEFAULT_TITLE = "test";

                @BeforeEach
                void setUp() {
                    Task task = new Task(TASK_DEFAULT_TITLE);
                    for (int i = 0; i < TASKS_MAX_SIZE; i++) {
                        task.setTitle(TASK_DEFAULT_TITLE + (i + 1));
                        taskService.createTask(task);
                    }
                }

                @Test
                @DisplayName("TaskNotFoundException 예외를 던집니다.")
                void detailWithInvalidId() throws Exception {
                    mockMvc.perform(get("/tasks/" + INVALID_REQUEST_TASK_ID))
                            .andExpect(status().isNotFound());
                }
            }
        }
    }

    @Nested
    @DisplayName("UPDATE")
    class Describe_update {
        private static final String UPDATE_TITLE = "otherTest";

        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
            objectMapper = new ObjectMapper();

            Task task = new Task(TASK_TITLE_ONE);

            taskService.createTask(task);
        }

        @Test
        @DisplayName("update 메소드는 tasks에서 클라이언트가 요청한 id에 해당하는 Task의 title을 변경해줍니다.")
        void update() throws Exception {
            assertThat(taskService.getTask(taskService.getTasksSize()).getTitle())
                    .isEqualTo(TASK_TITLE_ONE);

            Task task = new Task(UPDATE_TITLE);

            mockMvc.perform(put("/tasks/{taskId}",
                            taskService.getTasksSize())
                            .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(task)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(
                            containsString(UPDATE_TITLE)))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("DELETE")
    class Describe_delete {
        @BeforeEach
        void setUp() {
            Task task = new Task(TASK_TITLE_ONE);

            taskService.createTask(task);
        }

        @Test
        @DisplayName("delete 메소드는 Tasks에서 클라이언트가 요청한 id에 해당하는 Task를 지웁니다.")
        void deleteTask() throws Exception {
            assertThat(taskService.getTasks()).hasSize(1);

            mockMvc.perform(delete("/tasks/{taskId}",
                            taskService.getTasksSize()))
                    .andExpect(status().isNoContent());

            assertThat(taskService.getTasks()).hasSize(0);
        }
    }
}
