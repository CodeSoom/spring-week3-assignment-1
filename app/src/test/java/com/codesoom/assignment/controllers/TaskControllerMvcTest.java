package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.LongStream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@DisplayName("TaskController 클래스의")
class TaskControllerMvcTest {

    private static final String path = "/tasks/";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    private void addNumberOfTask(long n) {
        LongStream.rangeClosed(1, n).boxed()
                .forEach(id -> {
                    final String title = RandomTitleGenerator.getRandomTitle();
                    final Task task = new Task(id, title);

                    given(taskService.getTask(id)).willReturn(task);
                    registeredTaskMap.put(task.getId(), task);
                });

        given(taskService.getTasks()).willReturn(Collections.unmodifiableCollection(registeredTaskMap.values()));
    }

    private void addRandomNumberOfTask() {
        final long n = NumberGenerator.getRandomIntegerBetweenOneAndOneHundred();
        addNumberOfTask(n);
    }

    private Long getIdHavingMappedTask() {
        for (Long id : registeredTaskMap.keySet()) {
            return id;
        }

        throw new NoneTaskRegisteredException();
    }

    private Long getIdNotHavingMappedTask() {
        return Long.MAX_VALUE;
    }

    private ResultActions performGet(String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    private ResultActions performPost(String url, Task task) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)));
    }

    private ResultActions performUpdate(String url, RequestMethod requestMethod, Task task) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = requestMethod.equals(RequestMethod.PATCH) ? patch(url) : put(url);

        return mockMvc.perform(requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)));
    }

    private ResultActions performDelete(String url) throws Exception {
        return mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class collection_메소드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_task가_없는_경우 {

            @BeforeEach
            void setUp() {
                given(taskService.getTasks()).willReturn(Collections.emptyList());
            }

            @Test
            @DisplayName("응답 코드 200이다.")
            void it_responses_with_status_code_200() throws Exception {
                performGet(path).andExpect(status().isOk());
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

            @BeforeEach
            void setUp() {
                addRandomNumberOfTask();
            }

            @Test
            @DisplayName("응답 코드 200으로 응답한다.")
            void it_responses_with_status_code_200() throws Exception {
                performGet(path).andExpect(status().isOk());
            }

            @Test
            @DisplayName("등록된 task의 직렬화된 문자열을 포함한 response body로 응답한다.")
            void it_responses_with_body_containing_serialized_string_of_tasks_registered() throws Exception {
                performGet(path)
                        .andExpect(content().json(objectMapper.writeValueAsString(registeredTaskMap.values())));
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

            @BeforeEach
            void setUp() {
                id = NumberGenerator.getRandomNegativeLong();
            }

            @Test
            @DisplayName("400 응답 코드로 응답한다.")
            void it_responses_with_status_code_of_400() throws Exception {
                performGet(path + id).andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_task가_없을_때 {

            private Long id;

            @BeforeEach
            void setUp() {
                id = NumberGenerator.getRandomNotNegativeLong();

                given(taskService.getTask(id)).willThrow(new TaskNotFoundException(id));
            }

            @Test
            @DisplayName("응답 코드 404로 응답한다.")
            void it_responses_with_status_code_of_404() throws Exception {
                performGet(path + id).andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_task가_있을_때 {

            @BeforeEach
            void setUp() {
                addRandomNumberOfTask();
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 매핑된_task가_있는_id로_호출하면 {

                private Long id;

                @BeforeEach
                void setUp() {
                    id = getIdHavingMappedTask();
                }

                @Test
                @DisplayName("200 응답 코드와 해당 task의 json 형식 문자열을 담아 응답한다.")
                void it_responses_with_status_code_of_200() throws Exception {
                    final Task task = registeredTaskMap.get(id);

                    performGet(path + id).andExpect(status().isOk())
                            .andExpect(content().json(objectMapper.writeValueAsString(task)));
                }
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 매핑된_task가_없는_id로_호출하면 {

                private Long id;

                @BeforeEach
                void setUp() {
                    id = getIdNotHavingMappedTask();

                    given(taskService.getTask(id)).willThrow(new TaskNotFoundException(id));
                }

                @Test
                @DisplayName("404 응답 코드로 응답한다.")
                void it_responses_with_status_code_of_404_and_empty_response_body() throws Exception {
                    performGet(path + id).andExpect(status().isNotFound());
                }
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class create_메소드는 {

        private Task taskToBeGiven;
        private Task taskToBeReturned;

        @BeforeEach
        void setUp() {
            taskToBeGiven = new Task(null, RandomTitleGenerator.getRandomTitle());
            taskToBeReturned = new Task(1L, taskToBeGiven.getTitle());

            given(taskService.createTask(taskToBeGiven)).willReturn(taskToBeReturned);
        }

        @Test
        @DisplayName("응답 코드 201와 생성된 task의 json 형식 문자열로 응답한다.")
        void it_responses_with_status_code_of_204_and_response_body_of_json_of_task_registered() throws Exception {
            performPost(path, taskToBeGiven).andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(taskToBeReturned)));
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class update_메소드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class id가_음수라면 {

            private Long id;
            private Task task;

            @BeforeEach
            void setUp() {
                id = NumberGenerator.getRandomNegativeLong();
                task = new Task(null, RandomTitleGenerator.getRandomTitle());
            }

            @ParameterizedTest(name = "400 응답 코드와 빈 response body로 응답한다.")
            @EnumSource(mode = EnumSource.Mode.INCLUDE, names = {"PUT", "PATCH"})
            void it_responses_with_status_code_of_400_and_empty_response_body(RequestMethod requestMethod) throws Exception {
                performUpdate(path + id, requestMethod, task)
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class title이_비어_있거나_공백_문자로만_이루어진_경우 {

            private Long id = NumberGenerator.getRandomNotNegativeLong();
            private String[] titleList;

            @BeforeEach
            void setUp() {
                titleList = new String[]{"", "  "};
            }

            @ParameterizedTest(name = "400 응답 코드로 응답한다.")
            @EnumSource(names = {"PUT", "PATCH"})
            void it_responses_with_status_code_of_400(RequestMethod requestMethod) throws Exception {
                for (String title : titleList) {
                    final Task task = new Task(null, title);

                    performUpdate(path + id, requestMethod, task)
                            .andExpect(status().isBadRequest());
                }
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_task가_없을_때 {

            private Long id;
            private Task task;

            @BeforeEach
            void setUp() {
                id = NumberGenerator.getRandomNotNegativeLong();
                task = new Task(null, RandomTitleGenerator.getRandomTitle());

                given(taskService.updateTask(id, task)).willThrow(new TaskNotFoundException(id));
            }

            @ParameterizedTest(name = "404 응답 코드로 응답한다.")
            @EnumSource(names = {"PUT", "PATCH"})
            void it_responses_with_status_code_of_404(RequestMethod requestMethod) throws Exception {
                performUpdate(path + id, requestMethod, task)
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_task가_있을_때 {

            @BeforeEach
            void setUp() {
                addRandomNumberOfTask();
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 등록된_id로_호출하면 {

                private Long id;
                private Task taskWithNewTitle;
                private Task taskToBeReturned;

                @BeforeEach
                void setUp() {
                    id = getIdHavingMappedTask();
                    final String newTitle = RandomTitleGenerator.getRandomTitle();
                    taskWithNewTitle = new Task(null, newTitle);

                    taskToBeReturned = new Task(id, taskWithNewTitle.getTitle());
                    given(taskService.updateTask(id, taskWithNewTitle)).willReturn(taskToBeReturned);
                }

                @ParameterizedTest(name = "200 응답 코드와 title이 변경된 task의 Json 형식의 response body로 응답한다.")
                @EnumSource(names = {"PUT", "PATCH"})
                void it_responses_with_status_code_200_and_json_of_task_title_of_which_was_changed(RequestMethod requestMethod) throws Exception {
                    performUpdate(path + id, requestMethod, taskWithNewTitle)
                            .andExpect(status().isOk())
                            .andExpect(content().json(objectMapper.writeValueAsString(taskToBeReturned)));
                }
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 등록되지_않은_id로_호출하면 {

                private Long id;
                private Task taskWithNewTitle;

                @BeforeEach
                void setUp() {
                    id = getIdNotHavingMappedTask();
                    final String newTitle = RandomTitleGenerator.getRandomTitle();
                    taskWithNewTitle = new Task(null, newTitle);

                    given(taskService.updateTask(id, taskWithNewTitle)).willThrow(new TaskNotFoundException(id));
                }

                @ParameterizedTest(name = "404 응답 코드로 응답한다.")
                @EnumSource(names = {"PUT", "PATCH"})
                void it_responses_with_status_code_404(RequestMethod requestMethod) throws Exception {
                    performUpdate(path + id, requestMethod, taskWithNewTitle)
                            .andExpect(status().isNotFound());
                }
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class delete_메소드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class id가_음수라면 {

            private Long id;

            @BeforeEach
            void setUp() {
                id = NumberGenerator.getRandomNegativeLong();
            }

            @Test
            @DisplayName("400 응답 코드로 응답한다.")
            void it_responses_with_status_code_of_400() throws Exception {
                performDelete(path + id);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_task가_없을_때 {

            private Long id;

            @BeforeEach
            void setUp() {
                id = NumberGenerator.getRandomNotNegativeLong();
                given(taskService.deleteTask(id)).willThrow(new TaskNotFoundException(id));
            }

            @Test
            @DisplayName("응답 코드 404로 응답한다.")
            void it_responses_with_status_code_of_404() throws Exception {
                performDelete(path + id).andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_task가_있을_때 {

            @BeforeEach
            void setUp() {
                addRandomNumberOfTask();
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 매핑된_task가_있는_id로_호출하면 {

                private Long id;

                @BeforeEach
                void setUp() {
                    mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                            .addFilters(new CharacterEncodingFilter("UTF-8", true))
                            .alwaysDo(print())
                            .build();

                    id = getIdHavingMappedTask();
                    given(taskService.deleteTask(id)).willReturn(registeredTaskMap.get(id));
                }

                @Test
                @DisplayName("204 응답 코드로 응답한다.")
                void it_responses_with_status_code_of_204() throws Exception {
                    performDelete(path + id)
                            .andExpect(status().isNoContent());
                }
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 매핑된_task가_없는_id로_호출하면 {

                private Long id;

                @BeforeEach
                void setUp() {
                    id = getIdNotHavingMappedTask();
                    given(taskService.deleteTask(id)).willThrow(new TaskNotFoundException(id));
                }

                @Test
                @DisplayName("404 응답 코드로 응답한다.")
                void it_responses_with_status_code_of_404_and_empty_response_body() throws Exception {
                    performDelete(path + id).andExpect(status().isNotFound());
                }
            }
        }
    }
}
