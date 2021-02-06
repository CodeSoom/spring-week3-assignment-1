package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final Long givenSavedTaskId = 1L;
    private final Long givenUnsavedTaskId = 100L;
    private final String givenTaskTitle = "Test";
    private final String givenModifyTaskTitle = "Modified";

    private final int repeatTime = 2;

    private List<Task> taskList;
    private Task task;

    private RequestBuilder requestBuilder;
    private String uriTemplate;

    @Nested
    @DisplayName("요청을 받은 TaskController 클래스는")
    class Describe_TaskController_with_request {
        @BeforeEach
        void setUp() {
            taskList = new ArrayList<Task>();

            task = new Task();
            task.setId(givenSavedTaskId);
            task.setTitle(givenTaskTitle);
        }

        @Nested
        @DisplayName("요청이 GET /tasks 이고,")
        class Context_when_request_is_get_tasks {
            @BeforeEach
            void setRequest() {
                requestBuilder = get("/tasks");
            }

            @Nested
            @DisplayName("task가 없다면")
            class Context_without_any_task {
                @Test
                @DisplayName("비어 있는 리스트를 리턴한다.")
                void it_respond_200_ok_and_empty_list() throws Exception {
                    given(taskService.getTasks()).willReturn(new ArrayList<>());

                    mockMvc.perform(requestBuilder)
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString("[]")));
                }
            }

            @Nested
            @DisplayName("task가 1개 이상있다면")
            class Context_with_more_than_task {
                @BeforeEach
                void setAddedTask(RepetitionInfo repetitionInfo) {
                    for (int i = 0; i < repetitionInfo.getCurrentRepetition(); i++) {
                        taskList.add(task);
                    }

                    given(taskService.getTasks()).willReturn(taskList);
                }

                @RepeatedTest(repeatTime)
                @DisplayName("크기가 1이상인 리스트를 리턴한다.")
                void it_respond_200_ok_and_saved_task_list() throws Exception {
                    mockMvc.perform(requestBuilder)
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(givenTaskTitle)));
                }
            }
        }

        @Nested
        @DisplayName("요청이 GET /tasks/:id 이고")
        class Context_when_request_is_get_tasks_id {
            private String stringFormat = "/tasks/%d";

            @Nested
            @DisplayName("id값이 저장된 task의 id값과 동일하다면")
            class Context_when_id_is_equal_to_saved_task_id {
                @BeforeEach
                void setRequest() {
                    uriTemplate = String.format(stringFormat, givenSavedTaskId);
                    requestBuilder = get(uriTemplate);

                    taskList.add(task);

                    given(taskService.getTask(givenSavedTaskId)).willReturn(task);
                }

                @Test
                @DisplayName("저장된 task를 응답한다.")
                void it_respond_200_ok_and_saved_task() throws Exception {
                    mockMvc.perform(requestBuilder)
                            .andExpect(status().isOk())
                            .andExpect(content().string(containsString(givenTaskTitle)));
                }
            }

            @Nested
            @DisplayName("id값이 저장된 task의 id값과 동일하지 않다면")
            class Context_when_id_is_not_equal_to_saved_task_id {
                @BeforeEach
                void setRequest() {
                    uriTemplate = String.format(stringFormat, givenUnsavedTaskId);
                    requestBuilder = get(uriTemplate);

                    taskList.add(task);

                    given(taskService.getTask(givenUnsavedTaskId))
                            .willThrow(new TaskNotFoundException(givenUnsavedTaskId));
                }

                @Test
                @DisplayName("404 Not Found를 응답한다.")
                void it_respond_404_not_found() throws Exception {
                    mockMvc.perform(requestBuilder)
                            .andExpect(status().isNotFound());
                }
            }
        }
    }
}
