package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private static final String TITLE = "TEST_TITLE";

    private static final String UPDATE_TITLE = "UPDATE_TITLE";

    private static final Long DEFAULT_ID = 1L;

    private static final Long CREATE_ID = 2L;

    private static final Long INVALID_ID = 100L;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("Task 생성 테스트")
    class CreateTaskTest{
        
        @BeforeEach
        void setUp(){
            Task createTask = new Task();
            createTask.setId(CREATE_ID);
            createTask.setTitle(TITLE);
            given(taskService.createTask(any(Task.class))).willReturn(createTask);
        }
        @Test
        @DisplayName("Task 생성")
        void create() throws Exception {
            Task resource = new Task();
            resource.setTitle(TITLE);

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(resource)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("title",TITLE).exists());

        }

        @Nested
        @DisplayName("Task 리스트 테스트")
        class TasksTest{

            @BeforeEach
            void setUp(){

                List<Task> tasks = new ArrayList<>();

                Task task = new Task();
                task.setTitle(TITLE);
                task.setId(DEFAULT_ID);
                tasks.add(task);

                given(taskService.getTasks()).willReturn(tasks);
            }
            @Test
            @DisplayName("Task 리스트 조회")
            void list() throws Exception {

                mockMvc.perform(get("/tasks"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("[{\"id\":1,\"title\":\"TEST_TITLE\"}]"));
            }
        }
        
        @Nested
        @DisplayName("Task 상세조회 테스트")
        class TaskDetailTest{
            
            @BeforeEach
            void setUp(){
                Task task = new Task();
                task.setTitle(TITLE);
                task.setId(DEFAULT_ID);
                given(taskService.getTask(DEFAULT_ID)).willReturn(task);

                // 주석처리를 하지 않을 경우 아래 구문에서 예외 발생
                // given(taskService.getTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));
            }

            @Test
            @DisplayName("유효 ID 상세조회")
            void detailWithValidId() throws Exception {
                mockMvc.perform(get("/tasks/"+DEFAULT_ID))
                        .andExpect(status().isOk())
                        .andExpect(content().string("{\"id\":1,\"title\":\"TEST_TITLE\"}"));
            }

            @Test
            @DisplayName("유효하지 않는 ID 상세조회")
            void detailWithInvalidId() throws Exception {
                given(taskService.getTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));

                mockMvc.perform(get("/tasks/"+INVALID_ID))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string("{\"message\":\"Task not found\"}"));
            }
        }

        @Nested
        @DisplayName("Task 수정 테스트")
        class UpdateTest{

            @BeforeEach
            void setUp(){
                Task updateTask = new Task();
                updateTask.setId(DEFAULT_ID);
                updateTask.setTitle(UPDATE_TITLE);
                given(taskService.updateTask(eq(DEFAULT_ID), any(Task.class))).willReturn(updateTask);
                given(taskService.updateTask(eq(INVALID_ID), any(Task.class))).willThrow(new TaskNotFoundException(INVALID_ID));
            }

            @Test
            @DisplayName("[PUT] 유효 ID에 대한 Task 수정")
            void updateWithValidId() throws Exception {

                Task updateTask = new Task();
                updateTask.setTitle(UPDATE_TITLE);

                mockMvc.perform(put("/tasks/"+DEFAULT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateTask)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("title",UPDATE_TITLE).exists())
                        .andExpect(jsonPath("id",DEFAULT_ID).exists());

            }

            @Test
            @DisplayName("[PUT] 유효하지 않는 ID에 대한 Task 수정")
            void updateWithInvalidId() throws Exception {
                Task updateTask = new Task();
                updateTask.setTitle(UPDATE_TITLE);

                mockMvc.perform(put("/tasks/"+INVALID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateTask)))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string("{\"message\":\"Task not found\"}"));
            }

            @Test
            @DisplayName("[PATCH] 유효 ID에 대한 Task 수정")
            void patchWithValidId() throws Exception{
                Task updateTask = new Task();
                updateTask.setTitle(UPDATE_TITLE);

                mockMvc.perform(patch("/tasks/"+DEFAULT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateTask)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("title",UPDATE_TITLE).exists())
                        .andExpect(jsonPath("id",DEFAULT_ID).exists());
            }

            @Test
            @DisplayName("[PATCH] 유효하지 않는 ID에 대한 Task 수정")
            void patchWithInvalidId() throws Exception {
                Task updateTask = new Task();
                updateTask.setTitle(UPDATE_TITLE);

                mockMvc.perform(patch("/tasks/"+INVALID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateTask)))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string("{\"message\":\"Task not found\"}"));


            }
        }

        @Nested
        @DisplayName("Task 삭제 테스트")
        class DeleteTask{

            @Test
            @DisplayName("유효 ID에 대한 Task 삭제")
            void deleteWithValidId() throws Exception {

                given(taskService.deleteTask(DEFAULT_ID)).willReturn(null);

                mockMvc.perform(delete("/tasks/"+DEFAULT_ID))
                        .andExpect(status().isNoContent());

            }

            @Test
            @DisplayName("유효하지 않는 ID에 대한 Task 삭제")
            void deleteWithInvalidId() throws Exception {
                given(taskService.deleteTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));

                mockMvc.perform(delete("/tasks/"+INVALID_ID))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string("{\"message\":\"Task not found\"}"));
            }

        }
    }



   







    
}
