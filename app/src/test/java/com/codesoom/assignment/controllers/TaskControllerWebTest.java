package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskRepository;
import com.codesoom.assignment.TaskRepositoryCleaner;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskController Web API")
public class TaskControllerWebTest {
    public static final String FIXTURE_TITLE = "title";

    private TaskController controller;
    private TaskRepositoryCleaner repositoryCleaner;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        final TaskRepository repository = new TaskRepository();
        repositoryCleaner = new TaskRepositoryCleaner(repository);
        controller = new TaskController(new TaskService(repository));
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new TaskErrorAdvice())
                .build();
    }

    @Nested
    @DisplayName("GET /tasks 요청은")
    class Describe_getTasks {
        @Nested
        @DisplayName("생성되어 있는 할 일이 없다면")
        class Context_didNotCreateTask {
            @BeforeEach
            void prepare() {
                repositoryCleaner.deleteAllTasks();
            }

            @Test
            @DisplayName("OK status, 빈 목록을 반환한다")
            void it_returnsEmptyList() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[]"));
            }
        }

        @Nested
        @DisplayName("할 일을 생성했었을 때")
        class Context_didCreateContext {
            @BeforeEach
            void prepare() {
                final Task task1 = new Task(FIXTURE_TITLE + 1);
                controller.create(task1);

                final Task task2 = new Task(FIXTURE_TITLE + 2);
                controller.create(task2);
            }

            @Test
            @DisplayName("OK status, 생성된 할 일 목록을 반환한다")
            void it_returnsTasks() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[{\"id\":1,\"title\":\"title1\"},{\"id\":2,\"title\":\"title2\"}]"));
            }
        }
    }

    @Nested
    @DisplayName("GET /tasks/{id} 요청은")
    class Describe_getTasksWithId {
        @Nested
        @DisplayName("찾을 수 없는 id로 조회했을 때")
        class Context_withNotFindableTaskId {
            private final Long deletedTaskId = 1L;

            @BeforeEach
            void context() {
                controller.create(new Task(FIXTURE_TITLE));
                controller.delete(deletedTaskId);
            }

            @Test
            @DisplayName("NotFound status를 반환한다")
            void it_returnsTaskNotFoundStatus() throws Exception {
                mockMvc.perform(get("/tasks/" + deletedTaskId))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 id로 조회했을 떄")
        class Context_withFindableTaskId {
            @BeforeEach
            void prepare() {
                final Task task = new Task(FIXTURE_TITLE);
                controller.create(task);
            }

            @Test
            @DisplayName("OK status, 조회한 할 일을 반환한다")
            void it_returnsTasks() throws Exception {
                mockMvc.perform(get("/tasks/1"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("{\"id\":1,\"title\":\"title\"}"));
            }
        }
    }

    @Nested
    @DisplayName("POST /tasks 요청은")
    class Describe_postTasks {
        @Nested
        @DisplayName("request content를 담아서 요청하지 않았을 때")
        class Context_withoutBody {
            final String requestContent = "";

            @Test
            @DisplayName("BadRequest status를 반환한다")
            void it_returnsNotFoundStatus() throws Exception {
                mockMvc.perform(post("/tasks").content(requestContent))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("request content를 담아서 요청했을 때")
        class Context_withBody {
            final String requestContent = "{\"title\":\"title\"}";

            @Test
            @DisplayName("OK status를 반환한다")
            void it_returnsNotFoundStatus() throws Exception {
                mockMvc.perform(post("/tasks")
                                .content(requestContent)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());
            }
        }
    }
}
