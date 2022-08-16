package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;
//    @Autowired
//    private TaskErrorAdvice advice;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TaskService service;

    @BeforeEach
    void setUp() {

    }

    @Nested
    @DisplayName("경로 변수가 없는 GET /tasks는")
    class Describe_GET_NonPathVariable{

        @Test
        @DisplayName("응답 200을 반환한다")
        void It_Status200Return() throws Exception {
            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk());
        }

        @Nested
        @DisplayName("Task가 존재한다면")
        class Context_ExistsTasks{

            @Test
            @DisplayName("Task들을 JSON형식으로 반환한다")
            void It_AllTaskToJSONReturn() throws Exception {
                List<Task> tasks = new ArrayList<>();
                Task task1 = new Task(1L , "TEST1");
                Task task2 = new Task(2L , "TEST2");
                tasks.add(task1);
                tasks.add(task2);
                given(service.getTasks()).willReturn(tasks);

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(equalTo(mapper.writeValueAsString(tasks))));
            }
        }

        @Nested
        @DisplayName("Task가 존재하지 않는다면")
        class Context_NotExistsTask{
            @Test
            @DisplayName("빈 List를 반환한다")
            void It_EmptyListReturn() throws Exception {
                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(equalTo("[]")));
            }
        }
    }

    @Nested
    @DisplayName("경로 변수가 존재하는 GET /tasks/{id}는")
    class Describe_GET_PathVariable{

        @Nested
        @DisplayName("{id}에 해당하는 Task가 있다면")
        class Context_ExistsTask{

            @Test
            @DisplayName("상태 200 , 해당하는 Task를 JSON으로 반환한다")
            void It_JsonReturn() throws Exception {
                Task task1 = new Task(1L , "TEST1");
                given(service.getTask(1L)).willReturn(task1);

                mockMvc.perform(get("/tasks/1"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(equalTo(mapper.writeValueAsString(task1))));
            }
        }

        @Nested
        @DisplayName("{id}에 해당하는 Task가 없다면")
        class Context_NotExistsTask{

            @Test
            @DisplayName("상태 404 , TaskNotFoundException 발생 , message는 'Task not found'가 반환된다")
            void It_ErrorResultReturn() throws Exception {

                ErrorResponse response = new ErrorResponse("Task not found");
//                given(advice.handleNotFound()).willReturn(response);

                mockMvc.perform(get("/tasks/100"))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(containsString("Task not found")));
            }
        }
    }

    @Nested
    @DisplayName("POST /tasks는")
    class Describe_POST{

        @Nested
        @DisplayName("Task가 존재한다면")
        class Context_ExistsBodyTask{

            @Test
            @DisplayName("상태 201 , Task를 저장하고 저장된 정보를 반환한다")
            void It_SaveTask() throws Exception {
                Task newTask = new Task(1L , "TEST1");

                given(service.createTask(newTask)).willReturn(newTask);
                String content = mapper.writeValueAsString(newTask);

                mockMvc.perform(post("/tasks")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(equalTo(mapper.writeValueAsString(newTask))));
            }
        }
    }

}
