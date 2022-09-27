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
        @DisplayName("DB가 비어있을때 할 일 리스트를 조회한다면")
        class Context_with_empty_db {
            @Test
            @DisplayName("Status 200에 []를 리턴한다")
            void it_return_200_and_empty_array() throws Exception {
                // given
                given(taskService.getTasks()).willReturn(new ArrayList<>());

                // when
                ResultActions resultActions = subject();

                // then
                resultActions.andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string("[]"))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("DB에 데이터가 있을때 할 일 리스트를 조회한다면")
        class Context_with_non_empty_db extends NewTask {
            final String TITLE = "오늘의 할 일";

            @Test
            @DisplayName("Status 200에 모든 할 일을 리턴한다")
            void it_returns_200_and_all_tasks() throws Exception {
                // given
                List<Task> tasks = new ArrayList<>();
                Task task1 = withTitle(TITLE + "1");
                Task task2 = withTitle(TITLE + "2");

                tasks.add(task1);
                tasks.add(task2);

                given(taskService.getTasks()).willReturn(tasks);

                // when
                ResultActions resultActions = subject();

                // then
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
        final Long TASK_ID = 1L;
        final String TITLE = "1번 할 일";

        ResultActions subject() throws Exception {
            return mockMvc.perform(get("/tasks/" + TASK_ID));
        }

        @Nested
        @DisplayName("존재하지않는 ID로 할 일을 검색한다면")
        class Context_with_non_existing_task_id {
            @Test
            @DisplayName("Status 404와 메시지를 리턴한다")
            void it_returns_404_and_message() throws Exception {
                // given
                given(taskService.getTask(TASK_ID)).willThrow(new TaskNotFoundException(TASK_ID));

                // when
                ResultActions resultActions = subject();

                // then
                resultActions.andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("message", containsString("Task not found")))
                        .andDo(print());

            }
        }

        @Nested
        @DisplayName("존재하는 ID로 할 일을 검색한다면")
        class Context_with_existing_task_id extends NewTask {
            @Test
            @DisplayName("Status 200에 검색된 할 일을 리턴한다")
            void it_returns_200_and_found_task() throws Exception {
                // given
                given(taskService.getTask(TASK_ID)).willReturn(withTitle(TITLE));

                // when
                ResultActions resultActions = subject();

                // then
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
        final Long TASK_ID = 1L;
        final String TITLE = "등록된 할 일";

        @Nested
        @DisplayName("신규 할 일을 등록요청 한다면")
        class Context_with_new_task extends NewTask {
            @Test
            @DisplayName("등록 후 Status 201과 등록된 할 일을 리턴한다")
            void it_returns_201_and_registered_task() throws Exception {
                // given
                given(taskService.createTask(any(Task.class))).willReturn(withTitle(TITLE));

                // when
                ResultActions resultActions = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withIdAndTitle(TASK_ID, TITLE))));

                // then
                resultActions.andExpect(status().isCreated())
                        .andExpect(jsonPath("title").value(equalTo(TITLE)))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("update 메소드 [/tasks/id::PUT]는")
    class Describe_update {
        final Long TASK_ID = 1L;
        final String TITLE = "수정된 할 일";

        ResultActions subject(Task task) throws Exception {
            return mockMvc.perform(put("/tasks/" + TASK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(task)));
        }

        @Nested
        @DisplayName("존재하지않는 ID로 할 일을 수정요청 한다면")
        class context_with_non_existing_task_id extends NewTask {
            @Test
            @DisplayName("Status 404와 메시지를 리턴한다")
            void it_returns_404_and_message() throws Exception {
                // given

                given(taskService.updateTask(eq(TASK_ID), any(Task.class))).willThrow(new TaskNotFoundException(TASK_ID));

                // when
                ResultActions resultActions = subject(withTitle(TITLE));

                // then
                resultActions.andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("message", containsString("Task not found")))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("존재하는 ID로 할 일을 수정요청 한다면")
        class context_with_existing_task_id extends NewTask {
            @Test
            @DisplayName("Status 200와 수정된 할 일을 리턴한다")
            void it_returns_200_and_updated_task() throws Exception {
                // given
                Task task = withTitle(TITLE);

                given(taskService.updateTask(eq(TASK_ID), any(Task.class))).willReturn(task);

                // when
                ResultActions resultActions = subject(task);

                // then
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
        final Long TASK_ID = 1L;
        final String TITLE = "수정된 할 일";

        ResultActions subject(Task task) throws Exception {
            return mockMvc.perform(patch("/tasks/" + TASK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(task)));
        }

        @Nested
        @DisplayName("존재하지않는 ID로 할 일을 수정요청 한다면")
        class context_with_non_existing_task_id extends NewTask {
            @Test
            @DisplayName("Status 404와 메시지를 리턴한다")
            void it_returns_404_and_message() throws Exception {
                // given
                given(taskService.updateTask(eq(TASK_ID), any(Task.class))).willThrow(new TaskNotFoundException(TASK_ID));

                // when
                ResultActions resultActions = subject(withTitle(TITLE));

                // then
                resultActions.andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("message", containsString("Task not found")))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("존재하는 ID로 할 일을 수정요청 한다면")
        class context_with_existing_task_id extends NewTask {
            @Test
            @DisplayName("Status 200와 수정된 할 일을 리턴한다")
            void it_returns_200_and_updated_task() throws Exception {
                // given
                given(taskService.updateTask(eq(TASK_ID), any(Task.class))).willReturn(withIdAndTitle(TASK_ID, TITLE));

                // when
                ResultActions resultActions = subject(withTitle(TITLE));

                // then
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
        final Long TASK_ID = 1L;
        final String TITLE = "삭제된 할 일";

        ResultActions subject() throws Exception {
            return mockMvc.perform(delete("/tasks/" + TASK_ID)
                    .contentType(MediaType.APPLICATION_JSON));
        }

        @Nested
        @DisplayName("존재하지않는 ID로 할 일을 삭제요청 한다면")
        class context_with_non_existing_task_id {
            @Test
            @DisplayName("Status 404와 메시지를 리턴한다")
            void it_returns_404_and_message() throws Exception {
                // given
                given(taskService.deleteTask(TASK_ID)).willThrow(new TaskNotFoundException(TASK_ID));

                // when
                ResultActions resultActions = subject();

                // then
                resultActions.andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("message", containsString("Task not found")))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("존재하는 ID로 할 일을 삭제요청 한다면")
        class context_with_existing_task_id extends NewTask {
            @Test
            @DisplayName("Status 204와 수정된 할 일을 리턴한다")
            void it_returns_204() throws Exception {
                // given
                given(taskService.deleteTask(TASK_ID)).willReturn(withIdAndTitle(TASK_ID, TITLE));

                // when
                ResultActions resultActions = subject();

                // then
                resultActions.andExpect(status().isNoContent())
                        .andDo(print());
            }
        }
    }
}