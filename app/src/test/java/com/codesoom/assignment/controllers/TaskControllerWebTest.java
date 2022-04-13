package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController Web Layer")
public class TaskControllerWebTest {
    @Autowired MockMvc mockMvc;
    @Autowired TaskController taskController;
    private final String taskControllerPath = "/tasks";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("루트(/) 경로는")
    class Describe_root_path {
        private final String rootPath = "/";

        @Nested
        @DisplayName("GET 요청을 받는다면")
        class Context_with_get_request {
            private final ResultActions getRequest;

            public Context_with_get_request() throws Exception {
                this.getRequest = mockMvc.perform(get(taskControllerPath + rootPath));;
            }

            @Nested
            @DisplayName("Task 가 존재하지 않을 때")
            class Context_without_task {
                @Test
                @DisplayName("빈 리스트를 리턴한다.")
                void it_returns_empty_list() throws Exception {
                    getRequest.andExpect(content().string("[]"));
                }

                @Test
                @DisplayName("200 OK 로 응답한다.")
                void it_responses_200_ok() throws Exception {
                    getRequest.andExpect(status().isOk());
                }
            }

//            @Nested
//            @DisplayName("Task 가 있다면")
//            class Context_with_tasks {
//                private final int addSize = 2;
//
//                @BeforeEach
//                void add_task() {
//                    addTasks(addSize);
//                }
//
//                @Test
//                @DisplayName("Task 의 JSON 문자열을 리턴한다.")
//                void it_returns_tasks_as_json() throws Exception {
//                    getRequest.andExpect(content().string("[]"));
//                }
//
//                @Test
//                @DisplayName("200 OK 로 응답한다.")
//                void it_responses_200_ok() throws Exception {
//                    getRequest.andExpect(status().isOk());
//                }
//            }
        }
    }

    /**
     * number 만큼 Task 를 추가한다.
     * @param number
     */
    private void addTasks(int number) {
        for (int i = 1; i <= number; i++) {
            Task task = new Task();
            task.setTitle("task" + i);
            taskController.create(task);
        }
    }
}
