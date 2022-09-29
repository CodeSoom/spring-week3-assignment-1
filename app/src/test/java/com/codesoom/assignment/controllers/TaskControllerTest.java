package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.NewTask;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskControllerTest 클래스")
class TaskControllerTest {

    @Autowired
    private WebApplicationContext ctx;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setup() {
        // ResponseBody JSON에 한글이 깨지는 문제로 추가
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Nested
    @DisplayName("list 메소드 [/tasks::GET]는")
    class Describe_list {
        ResultActions subject() throws Exception {
            return mockMvc.perform(get("/tasks"));
        }

        @Nested
        @DisplayName("등록된 할 일이 존재하지 않는다면")
        class Context_with_empty_db {
            @BeforeEach
            void prepareTests() {
                given(taskService.getTasks()).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("OK(200)와 빈 데이터를 리턴한다")
            void it_return_200_and_empty_array() throws Exception {
                ResultActions resultActions = subject();

                resultActions.andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string("[]"))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("등록된 할 일이 존재한다면")
        class Context_with_non_empty_db extends NewTask {
            private final String TITLE = "오늘의 할 일";

            @BeforeEach
            void prepareTests() {
                List<Task> tasks = new ArrayList<>();
                Task task1 = withIdAndTitle(1L, TITLE + "1");
                Task task2 = withIdAndTitle(2L, TITLE + "2");

                tasks.add(task1);
                tasks.add(task2);

                given(taskService.getTasks()).willReturn(tasks);
            }

            @Test
            @DisplayName("OK(200)와 모든 할 일을 리턴한다")
            void it_returns_200_and_all_tasks() throws Exception {
                ResultActions resultActions = subject();

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].title").value(equalTo(TITLE + "1")))
                        .andExpect(jsonPath("$[1].title").value(equalTo(TITLE + "2")))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("datail 메소드 [/tasks/id::GET]는")
    class Describe_detail {
        private final Long TASK_ID = 1L;

        ResultActions subject() throws Exception {
            return mockMvc.perform(get("/tasks/" + TASK_ID));
        }

        @Nested
        @DisplayName("요청한 할 일 ID가 존재하지 않는다면")
        class Context_with_non_existing_task_id {
            private final String TITLE = "1번 할 일";

            @BeforeEach
            void prepareTests() {
                given(taskService.getTask(TASK_ID)).willThrow(new TaskNotFoundException(TASK_ID));
            }

            @Test
            @DisplayName("NOT_FOUND(404)와 예외 메시지를 리턴한다")
            void it_returns_404_and_message() throws Exception {
                ResultActions resultActions = subject();

                resultActions.andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("message", containsString("Task not found")))
                        .andDo(print());

            }
        }

        @Nested
        @DisplayName("요청한 할 일 ID가 존재한다면")
        class Context_with_existing_task_id extends NewTask {
            private final String TITLE = "1번 할 일";

            @BeforeEach
            void prepareTests() {
                given(taskService.getTask(TASK_ID)).willReturn(withTitle(TITLE));
            }

            @Test
            @DisplayName("OK(200)와 요청한 할 일을 리턴한다")
            void it_returns_200_and_found_task() throws Exception {
                ResultActions resultActions = subject();

                resultActions.andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("title").value(equalTo(TITLE)))
                        .andDo(print());

            }
        }
    }

    @Nested
    @DisplayName("create 메소드 [/tasks::POST]는")
    class Describe_create {
        @Nested
        @DisplayName("새로운 할 일을 등록요청 한다면")
        class Context_with_new_task extends NewTask {
            private final Long TASK_ID = 1L;
            private final String TITLE = "등록된 할 일";

            @BeforeEach
            void prepareTests() {
                given(taskService.createTask(any(Task.class))).willReturn(withTitle(TITLE));
            }

            @Test
            @DisplayName("DB에 등록하고 CREATED(201)와 등록된 할 일을 리턴한다")
            void it_returns_201_and_registered_task() throws Exception {
                ResultActions resultActions = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withIdAndTitle(TASK_ID, TITLE))));

                resultActions.andExpect(status().isCreated())
                        .andExpect(jsonPath("title").value(equalTo(TITLE)))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("update 메소드 [/tasks/id::PUT]는")
    class Describe_update {
        private final Long TASK_ID = 1L;

        ResultActions subject(Task task) throws Exception {
            return mockMvc.perform(put("/tasks/" + TASK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(task)));
        }

        @Nested
        @DisplayName("요청한 할 일 ID가 존재하지 않는다면")
        class Context_with_non_existing_task_id extends NewTask {
            private final String TITLE = "수정된 할 일";

            @BeforeEach
            void prepareTests() {
                given(taskService.updateTask(eq(TASK_ID), any(Task.class))).willThrow(new TaskNotFoundException(TASK_ID));
            }

            @Test
            @DisplayName("NOT_FOUND(404)와 예외 메시지를 리턴한다")
            void it_returns_404_and_message() throws Exception {
                ResultActions resultActions = subject(withTitle(TITLE));

                resultActions.andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("message", containsString("Task not found")))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("요청한 할 일 ID가 존재한다면")
        class Context_with_existing_task_id extends NewTask {
            private final String TITLE = "수정된 할 일";

            @BeforeEach
            void prepareTests() {
                given(taskService.updateTask(eq(TASK_ID), any(Task.class))).willReturn(withIdAndTitle(TASK_ID, TITLE));
            }

            @Test
            @DisplayName("DB를 수정하고 OK(200)와 수정된 할 일을 리턴한다")
            void it_returns_200_and_updated_task() throws Exception {
                ResultActions resultActions = subject(withTitle(TITLE));

                resultActions.andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("title").value(equalTo(TITLE)))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("patch 메소드 [/tasks/id::PATCH]는")
    class Describe_patch {
        private final Long TASK_ID = 1L;

        ResultActions subject(Task task) throws Exception {
            return mockMvc.perform(patch("/tasks/" + TASK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(task)));
        }
        @Nested
        @DisplayName("요청한 할 일 ID가 존재하지 않는다면")
        class Context_with_non_existing_task_id extends NewTask {
            private final String TITLE = "수정된 할 일";

            @BeforeEach
            void prepareTests() {
                given(taskService.updateTask(eq(TASK_ID), any(Task.class))).willThrow(new TaskNotFoundException(TASK_ID));
            }

            @Test
            @DisplayName("NOT_FOUND(404)와 예외 메시지를 리턴한다")
            void it_returns_404_and_message() throws Exception {
                ResultActions resultActions = subject(withTitle(TITLE));

                resultActions.andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("message", containsString("Task not found")))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("요청한 할 일 ID가 존재한다면")
        class Context_with_existing_task_id extends NewTask {
            private final String TITLE = "수정된 할 일";

            @BeforeEach
            void prepareTests() {
                given(taskService.updateTask(eq(TASK_ID), any(Task.class))).willReturn(withIdAndTitle(TASK_ID, TITLE));
            }

            @Test
            @DisplayName("DB를 수정하고 OK(200)와 수정된 할 일을 리턴한다")
            void it_returns_200_and_updated_task() throws Exception {
                ResultActions resultActions = subject(withTitle(TITLE));

                resultActions.andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("title").value(equalTo(TITLE)))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드 [/tasks/id::DELETE]는")
    class Describe_delete {
        private final Long TASK_ID = 1L;
        ResultActions subject() throws Exception {
            return mockMvc.perform(delete("/tasks/" + TASK_ID)
                    .contentType(MediaType.APPLICATION_JSON));
        }

        @Nested
        @DisplayName("요청한 할 일 ID가 존재하지 않는다면")
        class Context_with_non_existing_task_id {
            private final String TITLE = "삭제된 할 일";

            @BeforeEach
            void prepareTests() {
                given(taskService.deleteTask(TASK_ID)).willThrow(new TaskNotFoundException(TASK_ID));
            }

            @Test
            @DisplayName("NOT_FOUND(404)와 예외 메시지를 리턴한다")
            void it_returns_404_and_message() throws Exception {
                ResultActions resultActions = subject();

                resultActions.andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("message", containsString("Task not found")))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("요청한 할 일 ID가 존재한다면")
        class Context_with_existing_task_id extends NewTask {
            private final String TITLE = "삭제된 할 일";

            @BeforeEach
            void prepareTest() {
                given(taskService.deleteTask(TASK_ID)).willReturn(withIdAndTitle(TASK_ID, TITLE));
            }

            @Test
            @DisplayName("DB에서 삭제하고 NO_CONTENT(204)를 리턴한다")
            void it_returns_204() throws Exception {
                ResultActions resultActions = subject();

                resultActions.andExpect(status().isNoContent())
                        .andDo(print());
            }
        }
    }
}