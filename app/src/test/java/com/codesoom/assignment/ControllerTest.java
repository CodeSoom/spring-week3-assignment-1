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

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스")
public class ControllerTest extends TestHelper { // FIXME: 이름을 TaskControllerTest로 지으면 java 파일을 인식하지 못한다?!
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    private final Task dummyTask1 = dummyTask(1L, "1");
    private final Task dummyTask2 = dummyTask(2L, "2");
    private final Task dummyTask3 = dummyTask(3L, "3");

    @BeforeEach
    void setUp() throws Exception {
        /**
         * [고민]
         * 아무 task도 저장되어 있지 않은 mockMvc를 만들기 위해 모든 task의 id를 받아와 delete 하려 한다.
         * 하지만 모든 task의 id를 받아오기 위해선 GET /tasks 메소드를 이용해야 한다.
         * GET /tasks를 테스트하기 위한 사전 동작으로 GET /tasks를 이용해도 괜찮을까..?
         */

        MvcResult result = mockMvc.perform(get("/tasks")).andReturn();
        String content = result.getResponse().getContentAsString();
        List<Task> tasks = mapper.readValue(content, mapper.getTypeFactory().constructCollectionType(List.class, Task.class));
        for (Task task : tasks) {
            mockMvc.perform(delete("/tasks/" + task.getId()));
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
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("[]")));
            }
        }

        @Nested
        @DisplayName("등록된 task가 있다면")
        class Context_multiple_tasks {

            @BeforeEach
            void setUp() throws Exception {
                mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dummyTask1)));
                mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dummyTask2)));
                mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dummyTask3)));
            }

            @Test
            @DisplayName("200 OK와 task가 포함된 리스트를 반환한다")
            void it_returns_array() throws Exception {
                mockMvc.perform(get("/tasks"))
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
                mockMvc.perform(get("/task/" + Long.MAX_VALUE))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("해당 id를 가진 task가 있다면")
        class Context_multiple_tasks {
            private List<Task> tasks;

            @BeforeEach
            void setUp() throws Exception {
                mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dummyTask1)));
                mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dummyTask2)));
                mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dummyTask3)));

                MvcResult result = mockMvc.perform(get("/tasks")).andReturn();
                String content = result.getResponse().getContentAsString();
                tasks = mapper.readValue(content, mapper.getTypeFactory().constructCollectionType(List.class, Task.class));
            }

            @Test
            @DisplayName("200 OK와 task를 반환한다")
            void it_returns_task() throws Exception {
                for (Task task: tasks) {
                    mockMvc.perform(get("/tasks/" + task.getId()))
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
                mockMvc.perform(get("/tasks/invalid"))
                        .andExpect(status().isBadRequest());
            }
        }
    }

                mockMvc.perform(get("/tasks/" + dummyTask1.getId()))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(dummyTask1.getTitle())));
            }
        }
    }
}
