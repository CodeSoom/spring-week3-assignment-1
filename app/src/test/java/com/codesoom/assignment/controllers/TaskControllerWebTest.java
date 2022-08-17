package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerWebTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TaskService service;
    @MockBean
    private TaskErrorAdvice advice;

    private final String TASK_PATH = "/tasks";
    private final Long INVALID_ID = 100L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TaskController(service))
                .setControllerAdvice(advice)
                .build();
    }

    @AfterEach
    void tearDown() {
        reset(service);
        reset(advice);
    }

    List<Task> setTask(){
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task(1L , "TEST1");
        Task task2 = new Task(2L , "TEST2");
        tasks.add(task1);
        tasks.add(task2);
        given(service.getTasks()).willReturn(tasks);
        given(service.getTask(1L)).willReturn(task1);
        given(service.getTask(2L)).willReturn(task2);
        return tasks;
    }

    void setErrorAdvice(){
        given(service.getTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));
        given(advice.handleNotFound()).willReturn(new ErrorResponse("Task not found"));
    }

    @Nested
    @DisplayName("GET /tasks는")
    class Describe_GET_NonPathVariable{

        @Test
        @DisplayName("응답 200을 반환한다")
        void It_Status200Return() throws Exception {
            mockMvc.perform(get(TASK_PATH))
                    .andExpect(status().isOk());
        }

        @Nested
        @DisplayName("Task가 존재한다면")
        class Context_ExistsTasks{

            private List<Task> tasks;

            @BeforeEach
            void setUp(){
                tasks = setTask();
            }

            @Test
            @DisplayName("Task들을 JSON형식으로 반환한다")
            void It_AllTaskToJSONReturn() throws Exception {
                mockMvc.perform(get(TASK_PATH))
                        .andExpect(status().isOk())
                        .andExpect(content().string(equalTo(mapper.writeValueAsString(tasks))));
            }
        }

        @Nested
        @DisplayName("Task가 존재하지 않는다면")
        class Context_NotExistsTask{

            private final String EMPTY_ARR = "[]";

            @BeforeEach
            void setUp(){

            }

            @Test
            @DisplayName("빈 List를 반환한다")
            void It_EmptyListReturn() throws Exception {
                mockMvc.perform(get(TASK_PATH))
                        .andExpect(status().isOk())
                        .andExpect(content().string(equalTo(EMPTY_ARR)));
            }
        }
    }

    @Nested
    @DisplayName("GET /tasks/{id}는")
    class Describe_GET_PathVariable{

        @Nested
        @DisplayName("{id}에 해당하는 Task가 있다면")
        class Context_ExistsTask{

            List<Task> tasks;
            private final String TASK_ID = "1";

            @BeforeEach
            void setUp(){
                tasks = setTask();
            }

            @Test
            @DisplayName("상태 200 , 해당하는 Task를 JSON으로 반환한다")
            void It_JsonReturn() throws Exception {
                String content = mapper.writeValueAsString(tasks.get(Integer.parseInt(TASK_ID) - 1));
                mockMvc.perform(get(TASK_PATH+"/"+TASK_ID))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().string(equalTo(content)));
            }
        }

        @Nested
        @DisplayName("{id}에 해당하는 Task가 없다면")
        class Context_NotExistsTask{

            @BeforeEach
            void setUp(){
                setErrorAdvice();
            }

            @Test
            @DisplayName("상태 404 , TaskNotFoundException이 던져지고 message는 'Task not found'가 반환된다")
            void It_ErrorResultReturn() throws Exception {
                mockMvc.perform(get(TASK_PATH +"/"+ INVALID_ID))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(containsString("Task not found")));
            }
        }
    }

    @Nested
    @DisplayName("POST /tasks는")
    class Describe_POST{

        @Nested
        @DisplayName("body에 title이 존재한다면")
        class Context_ExistsBodyTitle{

            private final Long newId = 3L;
            private final Task newTask = new Task(newId , "UPDATE TITLE 1");

            @BeforeEach
            void setUp(){
                given(service.createTask(newTask)).willReturn(newTask);
                given(service.getTask(newId)).willReturn(newTask);
            }

            @Test
            @DisplayName("상태 201 , Task를 저장하고 저장된 정보를 반환한다")
            void It_SaveTask() throws Exception {
                String content = mapper.writeValueAsString(newTask);

                mockMvc.perform(post(TASK_PATH)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

                mockMvc.perform(get(TASK_PATH+"/"+newId))
                        .andExpect(status().isOk())
                        .andExpect(content().string(equalTo(content)));
            }
        }

        @Nested
        @DisplayName("body가 비어있다면")
        class Context_NotExistsBody{

            @Test
            @DisplayName("상태 400 , BadRequestException을 던진다")
            void It_ThrowException() throws Exception {
                mockMvc.perform(post(TASK_PATH))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("PUT,PATCH /tasks/{id}는")
    class Describe_PUT_PATCH{

        @Nested
        @DisplayName("{id}가 존재하지 않는다면")
        class Context_NotExistsId{

            @Test
            @DisplayName("상태 405 , MethodNotAllowedException을 던진다")
            void It_ThrowException() throws Exception {
                mockMvc.perform(put(TASK_PATH))
                        .andExpect(status().isMethodNotAllowed());

                mockMvc.perform(patch(TASK_PATH))
                        .andExpect(status().isMethodNotAllowed());
            }
        }

        @Nested
        @DisplayName("body가 존재하지 않는다면")
        class Context_NotExistsBody{

            private final String taskId = "1";

            @Test
            @DisplayName("상태 400 , BadRequestException을 던진다")
            void It_ThrowException() throws Exception {
                mockMvc.perform(put(TASK_PATH+"/"+taskId))
                        .andExpect(status().isBadRequest());

                mockMvc.perform(patch(TASK_PATH+"/"+taskId))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("body는 있지만 {id}에 해당하는 Task가 없다면")
        class Context_InvalidId{

            private final Task updateTask = new Task();

            @BeforeEach
            void setUp(){
                setErrorAdvice();
                updateTask.setTitle("Update Title");
            }

            @Test
            @DisplayName("상태 404 , TaskNotFoundException을 던진다")
            void It_ThrowException() throws Exception {
                String content = mapper.writeValueAsString(updateTask);
                mockMvc.perform(get(TASK_PATH +"/"+ INVALID_ID)
                                .content(content))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(containsString("Task not found")));
            }
        }

        @Nested
        @DisplayName("{id}와 body가 존재한다면")
        class Context_ExistsIdAndBody{

            private final Task beforeTask = new Task();
            private final Long updateId = 1L;
            @BeforeEach
            void setUp(){
                beforeTask.setTitle("Update Title");
                given(service.updateTask(updateId , beforeTask)).willReturn(beforeTask);
            }

            @Test
            @DisplayName("body에 담긴 title의 정보로 수정한다")
            void It_UpdateTask() throws Exception {
                String content = mapper.writeValueAsString(beforeTask);
                mockMvc.perform(put(TASK_PATH+"/"+updateId)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(equalTo(content)));
            }
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{id}는")
    class Describe_DELETE{

        @Nested
        @DisplayName("{id}가 존재하지 않는다면")
        class Context_NotExistsId{

            @Test
            @DisplayName("상태 405 , MethodNotAllowedException을 던진다")
            void It_ThrowException() throws Exception {
                mockMvc.perform(delete(TASK_PATH))
                        .andExpect(status().isMethodNotAllowed());
            }
        }

        @Nested
        @DisplayName("{id}에 해당하는 Task가 없다면")
        class Context_InvalidId{

            @BeforeEach
            void setUp(){
                setErrorAdvice();
            }

            @Test
            @DisplayName("상태 404 , TaskNotFoundException을 던진다")
            void It_ThrowException() throws Exception {
                mockMvc.perform(get(TASK_PATH +"/"+ INVALID_ID))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(containsString("Task not found")));
            }
        }

        @Nested
        @DisplayName("{id}에 해당하는 Task가 존재한다면")
        class Context_ValidId{

            private final Long validId = 1L;

            @Test
            @DisplayName("상태 204를 반환 , Task를 삭제한다")
            void It_DeleteTask() throws Exception {
                mockMvc.perform(delete(TASK_PATH+"/"+validId))
                        .andExpect(status().isNoContent());
            }
        }
    }
}
