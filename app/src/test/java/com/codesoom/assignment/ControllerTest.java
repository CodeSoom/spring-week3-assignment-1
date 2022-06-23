package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스")
public class ControllerTest extends TestHelper { // FIXME: 이름을 TaskControllerTest로 지으면 java 파일을 인식하지 못한다?!
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final Task dummyTask1 = dummyTask(1L, "1");
    private final Task dummyTask2 = dummyTask(2L, "2");
    private final Task dummyTask3 = dummyTask(3L, "3");

    private ResultActions performPost(String url, Object object) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(object)));
    }

    private ResultActions performGet(String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    private ResultActions performDelete(String url) throws Exception {
        return mockMvc.perform(delete(url));
    }

    private ResultActions performPut(String url, Object object) throws Exception {
        return mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(object)));
    }

    private ResultActions performPatch(String url, Object object) throws Exception {
        return mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(object)));
    }

    @BeforeEach
    void setUp() throws Exception {
        /**
         * [고민]
         * 아무 task도 저장되어 있지 않은 mockMvc를 만들기 위해 모든 task의 id를 받아와 delete 하려 한다.
         * 하지만 모든 task의 id를 받아오기 위해선 GET /tasks 메소드를 이용해야 한다.
         * GET /tasks를 테스트하기 위한 사전 동작으로 GET /tasks를 이용해도 괜찮을까..?
         */

        MvcResult result = performGet("/tasks").andReturn();
        String content = result.getResponse().getContentAsString();
        List<Task> tasks = mapper.readValue(content, mapper.getTypeFactory().constructCollectionType(List.class, Task.class));
        for (Task task : tasks) {
            performDelete("/tasks/" + task.getId());
        }
    }

    @Nested
    @DisplayName("GET /tasks API는")
    class Describe_GET_tasks {

        @Nested
        @DisplayName("등록된 task가 없다면")
        class Context_no_tasks {

            @Test
            @DisplayName("200 OK와 빈 리스트를 리턴한다")
            void it_returns_empty_array() throws Exception {
                performGet("/tasks")
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("[]")));
            }
        }

        @Nested
        @DisplayName("등록된 task가 있다면")
        class Context_multiple_tasks {

            @BeforeEach
            void setUp() throws Exception {
                performPost("/tasks", dummyTask1);
                performPost("/tasks", dummyTask2);
                performPost("/tasks", dummyTask3);
            }

            @Test
            @DisplayName("200 OK와 task가 포함된 리스트를 반환한다")
            void it_returns_array() throws Exception {
                performGet("/tasks")
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(dummyTask1.getTitle())))
                        .andExpect(content().string(containsString(dummyTask2.getTitle())))
                        .andExpect(content().string(containsString(dummyTask3.getTitle())));
            }
        }
    }

    @Nested
    @DisplayName("GET /tasks/{id} API는")
    class Describe_GET_tasks_id {

        @Nested
        @DisplayName("해당 id를 가진 task가 없다면")
        class Context_no_tasks {

            @Test
            @DisplayName("404 NOT FOUND 예외를 던진다")
            void it_throws_404_exception() throws Exception {
                performGet("/tasks/1")
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("해당 id를 가진 task가 있다면")
        class Context_multiple_tasks {
            private List<Task> tasks;

            @BeforeEach
            void setUp() throws Exception {
                performPost("/tasks", dummyTask1);
                performPost("/tasks", dummyTask2);
                performPost("/tasks", dummyTask3);

                MvcResult result = performGet("/tasks").andReturn();
                String content = result.getResponse().getContentAsString();
                tasks = mapper.readValue(content, mapper.getTypeFactory().constructCollectionType(List.class, Task.class));
            }

            @Test
            @DisplayName("200 OK와 task를 반환한다")
            void it_returns_task() throws Exception {
                for (Task task: tasks) {
                    performGet("/tasks/" + task.getId())
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(task.getTitle())));
                }
            }
        }

        @Nested
        @DisplayName("정상적인 id를 요청하지 않으면")
        class Context_invalid_id {

            @Test
            @DisplayName("400 BAD REQUEST 예외를 던진다")
            void it_returns_400_exception() throws Exception {
                performGet("/tasks/invalid")
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("POST /tasks API는")
    class Describe_POST_tasks {

        @Nested
        @DisplayName("정상적인 요청이라면")
        class Context_valid_request {

            @Test
            @DisplayName("201 CREATED와 생성된 Task를 반환한다")
            void it_returns_created_task() throws Exception {
                performPost("/tasks", dummyTask1)
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(dummyTask1.getTitle())));
            }
        }

        @Nested
        @DisplayName("Request body가 없는 요청이라면")
        class Context_no_request_body {

            @Test
            @DisplayName("400 BAD REQUEST 예외를 던진다")
            void it_returns_400_exception() throws Exception {
                mockMvc.perform(post("/tasks"))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("Request body에 Task가 없는 요청이라면")
        class Context_no_task {

            @Test
            @DisplayName("400 BAD REQUEST 예외를 던진다")
            void it_returns_400_exception() throws Exception {
                performPost("/tasks", "")
                        .andExpect(status().isBadRequest());
            }
        }

    }


    @Nested
    @DisplayName("PUT /tasks/{id} API는")
    class Describe_PUT_tasks_id {

        @Nested
        @DisplayName("정상적인 요청이라면")
        class Context_valid_request {
            private Task task;

            @BeforeEach
            void setUp() throws Exception {
                MvcResult result = performPost("/tasks", dummyTask1).andReturn();
                String content = result.getResponse().getContentAsString();
                task = mapper.readValue(content, Task.class);
            }

            @Test
            @DisplayName("200 OK와 수정된 Task를 반환한다")
            void it_returns_updated_task() throws Exception {
                System.out.println(task.getId());
                performPut("/tasks/" + task.getId(), dummyTask2)
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(dummyTask2.getTitle())));
            }
        }

        @Nested
        @DisplayName("해당 id를 가진 task가 없다면")
        class Context_no_tasks {

            @Test
            @DisplayName("404 NOT FOUND 예외를 던진다")
            void it_throws_404_exception () throws Exception {
                performPut("/tasks/1", dummyTask2)
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("Request body가 없는 요청이라면")
        class Context_no_request_body {

            @Test
            @DisplayName("400 BAD REQUEST 예외를 던진다")
            void it_returns_400_exception() throws Exception {
                mockMvc.perform(put("/tasks/1"))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("Request body에 Task가 없는 요청이라면")
        class Context_no_task {

            @Test
            @DisplayName("400 BAD REQUEST 예외를 던진다")
            void it_returns_400_exception() throws Exception {
                performPut("/tasks/1", "")
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("PATCH /tasks/{id} API는")
    class Describe_PATCH_tasks_id {

        @Nested
        @DisplayName("정상적인 요청이라면")
        class Context_valid_request {
            private Task task;

            @BeforeEach
            void setUp() throws Exception {
                MvcResult result = performPost("/tasks", dummyTask1).andReturn();
                String content = result.getResponse().getContentAsString();
                task = mapper.readValue(content, Task.class);
            }

            @Test
            @DisplayName("200 OK와 수정된 Task를 반환한다")
            void it_returns_updated_task() throws Exception {
                performPatch("/tasks/" + task.getId(), dummyTask2)
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(dummyTask2.getTitle())));
            }
        }

        @Nested
        @DisplayName("해당 id를 가진 task가 없다면")
        class Context_no_tasks {

            @Test
            @DisplayName("404 NOT FOUND 예외를 던진다")
            void it_throws_404_exception () throws Exception {
                performPatch("/tasks/1", dummyTask2)
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("Request body가 없는 요청이라면")
        class Context_no_request_body {

            @Test
            @DisplayName("400 BAD REQUEST 예외를 던진다")
            void it_returns_400_exception() throws Exception {
                mockMvc.perform(patch("/tasks/1"))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("Request body에 Task가 없는 요청이라면")
        class Context_no_task {

            @Test
            @DisplayName("400 BAD REQUEST 예외를 던진다")
            void it_returns_400_exception() throws Exception {
                performPatch("/tasks/1", "")
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{id} API는")
    class Describe_DELETE_tasks_id {

        @Nested
        @DisplayName("정상적인 요청이라면")
        class Context_valid_request {
            private Task task;

            @BeforeEach
            void setUp() throws Exception {
                MvcResult result = performPost("/tasks", dummyTask1).andReturn();
                String content = result.getResponse().getContentAsString();
                task = mapper.readValue(content, Task.class);
            }

            @Test
            @DisplayName("204 NO CONTENT와 비어있는 body를 반환한다")
            void it_returns_204_no_content() throws Exception {
                performDelete("/tasks/" + task.getId())
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("해당 id를 가진 task가 없다면")
        class Context_no_tasks {

            @Test
            @DisplayName("404 NOT FOUND 예외를 던진다")
            void it_throws_404_exception () throws Exception {
                performDelete("/tasks/1")
                        .andExpect(status().isNotFound());
            }
        }
    }


}
