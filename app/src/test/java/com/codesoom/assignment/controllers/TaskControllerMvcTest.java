package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.exceptions.NegativeIdException;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
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

    private List<Task> registeredTaskList;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
                .alwaysDo(print())
                .build();

        registeredTaskList = new ArrayList<>();
    }

    void addNumberOfTask(long n) {
        LongStream.rangeClosed(1, n).boxed()
                .forEach(id -> {
                    final String title = RandomTitleGenerator.getRandomTitle();
                    final Task task = new Task(id, title);

                    given(taskService.getTask(id)).willReturn(task);
                    registeredTaskList.add(task);
                });

        given(taskService.getTasks()).willReturn(registeredTaskList);
    }

    void addRandomNumberOfTask() {
        final long n = NumberGenerator.getRandomIntegerBetweenOneAndOneHundred();
        addNumberOfTask(n);
    }

    private ResultActions performGet(String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class colletion_메소드는 {

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
            @DisplayName("빈 컬렉션을 반환한다.")
            void 빈_컬렉션을_반환한다() throws Exception {
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
                for (int i = 0; i < registeredTaskList.size(); i++) {
                    final Task task = registeredTaskList.get(i);
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
                resultActions = performGet(path + id);
                given(taskService.getTask(id)).willThrow(new NegativeIdException(id));
            }

            @Test
            @DisplayName("400 응답 코드로 응답한다.")
            void it_reponses_with_status_code_of_400() throws Exception {
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
                resultActions = performGet(path + id);
                given(taskService.getTask(id)).willThrow(new TaskNotFoundException(id));
            }

            @Test
            @DisplayName("응답 코드 404로 응답한다.")
            void it_responses_with_status_code_of_404() throws Exception {
                resultActions.andExpect(status().isNotFound());
            }
        }


    }
}
