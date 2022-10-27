package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.exceptions.NegativeIdException;
import com.codesoom.assignment.exceptions.NoneTaskRegisteredException;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.utils.NumberGenerator;
import com.codesoom.assignment.utils.RandomTitleGenerator;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController 클래스의")
class TaskControllerMvcTest {

    private static final String path = "/tasks/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private TaskService taskService;

    private Map<Long, Task> registeredTaskMap;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
                .alwaysDo(print())
                .build();

        registeredTaskMap = new TreeMap<>();
    }

    void addNumberOfTask(long n) {
        LongStream.rangeClosed(1, n).boxed()
                .forEach(id -> {
                    final String title = RandomTitleGenerator.getRandomTitle();
                    final Task task = new Task(id, title);

                    given(taskService.getTask(id)).willReturn(task);
                    registeredTaskMap.put(task.getId(), task);
                });

        given(taskService.getTasks()).willReturn(registeredTaskMap.values());
    }

    void addRandomNumberOfTask() {
        final long n = NumberGenerator.getRandomIntegerBetweenOneAndOneHundred();
        addNumberOfTask(n);
    }

    Long getIdHavingMappedTask() {
        for (Long id : registeredTaskMap.keySet()) {
            return id;
        }

        throw new NoneTaskRegisteredException();
    }

    Long getIdNotHavingMappedTask() {
        return Long.MAX_VALUE;
    }

    private ResultActions performGet(String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    private ResultActions performPost(String url, Object o) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(o))
        );
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class collection_메소드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_task가_없는_경우 {

            private ResultActions resultActions;

            @BeforeEach
            void setUp() throws Exception {
                given(taskService.getTasks()).willReturn(Collections.emptyList());
                resultActions = performGet(path);
            }

            @Test
            @DisplayName("응답 코드 200이다.")
            void it_responses_with_status_code_200() throws Exception {
                resultActions.andExpect(status().isOk());
            }

            @Test
            @DisplayName("빈 response body를 반환한다.")
            void it_responses_with_empty_response_body() throws Exception {
                performGet(path).andExpect(content().string("[]"));
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_task가_있는_경우 {

            private ResultActions resultActions;

            @BeforeEach
            void setUp() throws Exception {
                addRandomNumberOfTask();
                resultActions = performGet(path);
            }

            @Test
            @DisplayName("응답 코드 200이다.")
            void it_responses_with_status_code_200() throws Exception {
                resultActions.andExpect(status().isOk());
            }

            @Test
            @DisplayName("등록된 task의 직렬화된 문자열을 포함한 response body로 응답한다.")
            void it_responses_with_body_containing_serialized_string_of_tasks_registered() throws Exception {
                for (Task task : registeredTaskMap.values()) {
                    final String json = objectMapper.writeValueAsString(task);

                    resultActions.andExpect(content().string(containsString(json)));
                }
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class detail_메소드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 음수인_id를_인자로_호출하면 {

            private Long id;
            private ResultActions resultActions;

            @BeforeEach
            void setUp() throws Exception {
                id = NumberGenerator.getRandomNegativeLong();
                given(taskService.getTask(id)).willThrow(new NegativeIdException(id));
                resultActions = performGet(path + id);
            }

            @Test
            @DisplayName("400 응답 코드로 응답한다.")
            void it_responses_with_status_code_of_400() throws Exception {
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_task가_없으면 {

            private Long id;
            private ResultActions resultActions;

            @BeforeEach
            void setUp() throws Exception {
                id = NumberGenerator.getRandomNotNegativeLong();
                given(taskService.getTask(id)).willThrow(new TaskNotFoundException(id));
                resultActions = performGet(path + id);
            }

            @Test
            @DisplayName("응답 코드 404로 응답한다.")
            void it_responses_with_status_code_of_404() throws Exception {
                resultActions.andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_task가_있다면 {

            @BeforeEach
            void setUp() {
                addRandomNumberOfTask();
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 매핑된_task가_있는_id로_호출하면 {

                private Long id;
                private ResultActions resultActions;

                @BeforeEach
                void setUp() throws Exception {
                    id = getIdHavingMappedTask();
                    resultActions = performGet(path + id);
                }

                @Test
                @DisplayName("200 응답 코드와 해당 task의 json 형식 문자열을 담아 응답한다.")
                void it_responses_with_status_code_of_200() throws Exception {
                    final Task task = registeredTaskMap.get(id);
                    final String json = objectMapper.writeValueAsString(task);
                    resultActions.andExpect(status().isOk())
                            .andExpect(content().string(containsString(json)));
                }
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 매핑된_task가_없는_id로_호출하면 {

                private Long id;
                private ResultActions resultActions;

                @BeforeEach
                void setUp() throws Exception {
                    id = getIdNotHavingMappedTask();
                    given(taskService.getTask(id)).willThrow(new TaskNotFoundException(id));

                    resultActions = performGet(path + id);
                }

                @Test
                @DisplayName("404 응답 코드로 응답한다.")
                void it_responses_with_status_code_of_404_and_empty_response_body() throws Exception {
                    resultActions.andExpect(status().isNotFound());
                }
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class create_메소드는 {

        private Task taskToBeGiven;
        private Task taskToBeReturned;
        private ResultActions resultActions;

        @BeforeEach
        void setUp() throws Exception {
            taskToBeGiven = new Task(null, RandomTitleGenerator.getRandomTitle());
            taskToBeReturned = new Task(1L, taskToBeGiven.getTitle());
            given(taskService.createTask(taskToBeGiven)).willReturn(taskToBeReturned);

            resultActions = performPost(path, taskToBeGiven);
        }

        @Test
        @DisplayName("응답 코드 204와 생성된 task의 json 형식 문자열로 응답한다.")
        void it_responses_with_status_code_of_204_and_response_body_of_json_of_task_registered() throws Exception {
            resultActions.andExpect(status().isCreated())
                    .andExpect(content().string(containsString(objectMapper.writeValueAsString(taskToBeReturned))));
        }
    }
}
