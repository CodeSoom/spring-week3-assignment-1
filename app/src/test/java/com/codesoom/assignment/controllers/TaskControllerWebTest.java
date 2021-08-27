package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@DisplayName("TaskControllerWebTest 클래스")
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    private final String[] TASK_TITLE = {"test1", "test2", "test3", "test4", "test5"};
    private final String TASK_UPDATE = "update";
    private final Long VALID_ID = 1L;
    private final Long INVALID_ID = 100L;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskService taskService; // 스프링에서 DI 주입


    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("tasks 리스트에 값이 존재하면")
        class Context_exist_tasks {

            @BeforeEach
            @DisplayName("tasks 리스트를 초기화합니다")
            void list_setUp() {

                List<Task> tasks = new ArrayList<>();
                given(taskService.getTasks()).willReturn(tasks);

            }

            @Test
            @DisplayName("task 객체들을 리턴하고 200 상태코드를 내려준다")
            void It_return_tasks_status_200() throws Exception {

                mockMvc.perform(get("/tasks")).andExpect(status().isOk());

            }

        }

    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("list에 id가 존재하면")
        class Context_exist_id {

            @BeforeEach
            @DisplayName("Task 객체를 세팅합니다")
            void task_setUp() {

                taskService.createTask(new Task());

                Task foundTask = new Task();
                given(taskService.getTask(VALID_ID)).willReturn(foundTask);

            }

            @Test
            @DisplayName("Task 객체를 리턴해주고 200 상태코드를 내려준다")
            void It_return_task_status_200() throws Exception {

                mockMvc.perform(get("/tasks/"+VALID_ID)).andExpect(status().isOk());

            }

        }

        @Nested
        @DisplayName("list에 id가 존재하지 않는다면")
        class Context_exist_not_id {

            @BeforeEach
            @DisplayName("Invalid Task 객체를 세팅합니다")
            void invalid_task_setUp() {

                given(taskService.getTask(INVALID_ID)).willThrow(TaskNotFoundException.class);

            }

            @Test
            @DisplayName("TaskNotFoundException 예외를 던져주고 404 에러를 내려준다")
            void It_return_exception_status_404() throws Exception {

                mockMvc.perform(get("/tasks/"+INVALID_ID)).andExpect(status().isNotFound());

            }

        }

    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("추가할 Task 객체가 존재한다면")
        class Context_exist_task {

            Task task = new Task();

            @BeforeEach
            @DisplayName("Task 객체를 세팅합니다")
            void task_setUp() {

                task.setTitle(TASK_TITLE[0]);
                given(taskService.createTask(task)).willReturn(task);

            }

            @Test
            @DisplayName("tasks 리스트에 추가하고 201 상태코드를 내려준다")
            void It_add_tasks_status_201() throws Exception {

                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(task)))
                        .andExpect(status().isCreated());

            }

        }

    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("수정하고 싶은 id가 존재하면")
        class Context_exist_update_id {

            Task updateTask = new Task();

            @BeforeEach
            @DisplayName("수정할 Task 객체를 세팅합니다")
            void update_setUp() {

                Task task = new Task();
                task.setTitle(TASK_TITLE[0]);
                taskService.createTask(task);

                updateTask.setTitle(TASK_TITLE[0] + TASK_UPDATE);
                given(taskService.updateTask(INVALID_ID, updateTask)).willReturn(updateTask);

            }

            @Test
            @DisplayName("list에서 id를 찾아 값을 수정한다")
            void It_update() throws Exception {

                mockMvc.perform(put("/tasks/"+VALID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateTask)))
                        .andExpect(status().isOk());

                mockMvc.perform(patch("/tasks/"+VALID_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateTask)))
                        .andExpect(status().isOk());

            }

        }

    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("삭제하고 싶은 id가 존재하면")
        class Context_exist_delete_id {

            Task deleteTask = new Task();

            @BeforeEach
            @DisplayName("Task 객체를 세팅합니다")
            void delete_setUp() {

                for (int i = 0; i < TASK_TITLE.length; i++) {
                    Task task = new Task();
                    task.setTitle(TASK_TITLE[i]);
                    taskService.createTask(task);
                }

                given(taskService.deleteTask(VALID_ID)).willReturn(deleteTask);
            }

            @Test
            @DisplayName("list에서 id를 찾아 Task 객체를 삭제하고 204 상태코드를 내려준다")
            void It_delete_task_status_204() throws Exception {

                mockMvc.perform(delete("/tasks/"+VALID_ID)).andExpect(status().isNoContent());

            }

        }

    }

}
