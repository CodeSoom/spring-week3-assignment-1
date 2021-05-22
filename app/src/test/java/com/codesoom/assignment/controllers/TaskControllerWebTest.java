package com.codesoom.assignment.controllers;

import com.codesoom.assignment.common.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskService;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TaskController의 Mockito를 활용한 단위 테스트")
class TaskControllerWebTest {

    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private final Long NOT_FOUND_TASK_ID = 100L; // 목록에 없는 할 일 ID
    private final Long NEW_TASK_ID = 1L; // 새로 생성할 할 일 ID

    private final String NEW_TASK_TITLE = "Test Title"; // 새로 생성할 할 일 제목
    private final String UPDATE_TASK_TITLE = "Test Title Updated"; // 수정된 할 일 제목
    private final String TASK_NOT_FOUND_ERROR_MESSAGE = "존재하지 않는 Task이기 때문에 찾을 수 없습니다.";


    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("만약 목록이 비어있다면")
        class Context_empty {

            @BeforeEach
            void setUp() {
                List<Task> tasks = new ArrayList<>();
                given(taskService.getTaskList()).willReturn(tasks);
            }

            @Test
            @DisplayName("빈 할 일 목록과 200 상태코드를 반환합니다.")
            void it_return_emptyList_and_200_status() throws Exception {

                mockMvc.perform(get("/tasks"))
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string("[]"))
                        .andExpect(status().isOk());
            }

        }

        @Nested
        @DisplayName("만약 목록이 비어있지 않다면")
        class Context_not_empty {

            private List<Task> tasks = new ArrayList<>();
            @BeforeEach
            void setUp() {
                Task newTask = new Task();
                newTask.setId(NEW_TASK_ID);
                newTask.setTitle(NEW_TASK_TITLE);
                tasks.add(newTask);
                given(taskService.getTaskList()).willReturn(tasks);
            }

            @Test
            @DisplayName("할 일 목록과 200 상태코드를 반환합니다.")
            void it_return_list_and_200_status() throws Exception {

                mockMvc.perform(get("/tasks"))
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().json(objectMapper.writeValueAsString(tasks)))
                        .andExpect(status().isOk());

            }

        }

    }


    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 조회한다면")
        class Context_invalid_task_id {

            @BeforeEach
            void setUp() {
                given(taskService.findTaskOne(NOT_FOUND_TASK_ID)).willThrow(new TaskNotFoundException());
            }

            @Test
            @DisplayName("404 상태코드와 에러 메세지를 반환합니다.")
            void it_return_404_status_and_err_message() throws Exception {
                mockMvc.perform(get("/tasks/"+NOT_FOUND_TASK_ID))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(TASK_NOT_FOUND_ERROR_MESSAGE));
            }

        }

        @Nested
        @DisplayName("만약 할 일 목록에 있는 할 일을 조회한다면")
        class Context_valid_task_id {

            private Long findTaskId = NEW_TASK_ID;

            @BeforeEach
            void setUp() {

                List<Task> tasks = new ArrayList<>();

                Task newTask = new Task();
                newTask.setId(NEW_TASK_ID);
                newTask.setTitle(NEW_TASK_TITLE);
                tasks.add(newTask);

                given(taskService.findTaskOne(findTaskId)).willReturn(newTask);

            }

            @Test
            @DisplayName("200 상태코드와 조회된 할 일을 반환합니다.")
            void it_return_found_task_and_200_status() throws Exception {
                mockMvc.perform(get("/tasks/"+findTaskId))
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(NEW_TASK_TITLE)));
            }

        }

    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("만약 할 일 생성 요청을 보낸다면")
        class Context_valid_task_id {

            private Task newTask = new Task();

            @BeforeEach
            void setUp() {

                newTask.setId(NEW_TASK_ID);
                newTask.setTitle(NEW_TASK_TITLE);

                given(taskService.saveNewTask(newTask.getTitle())).willReturn(newTask);

            }

            @Test
            @DisplayName("201 상태코드와 생성된 할 일을 반환합니다.")
            void it_return_created_task_and_201_status() throws Exception {

                mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(NEW_TASK_TITLE)));

            }

        }

    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 수정한다면")
        class Context_invalid_task_id {

            private Task paramTask = new Task();

            @BeforeEach
            void setUp() {
                paramTask.setTitle(NEW_TASK_TITLE);
                given(taskService.updateTask(NOT_FOUND_TASK_ID, paramTask.getTitle())).willThrow(new TaskNotFoundException());
            }

            @Test
            @DisplayName("404 상태코드와 에러 메세지를 반환합니다.")
            void it_return_404_status_and_err_message() throws Exception {
                mockMvc.perform(put("/tasks/"+NOT_FOUND_TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paramTask)))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(TASK_NOT_FOUND_ERROR_MESSAGE));
            }

        }

        @Nested
        @DisplayName("만약 할 일 목록에 있는 할 일을 수정한다면")
        class Context_valid_task_id {

            private Long updateTaskId = NEW_TASK_ID;
            private Task paramTask = new Task();

            @BeforeEach
            void setUp() {
                List<Task> tasks = new ArrayList<>();

                Task newTask = new Task();
                newTask.setId(NEW_TASK_ID);
                newTask.setTitle(NEW_TASK_TITLE);

                tasks.add(newTask);

                paramTask = new Task();
                paramTask.setId(updateTaskId);
                paramTask.setTitle(UPDATE_TASK_TITLE);

                given(taskService.updateTask(paramTask.getId(), UPDATE_TASK_TITLE)).willReturn(paramTask);
            }

            @Test
            @DisplayName("200 상태코드와 수정된 할 일을 반환합니다.")
            void it_return_updated_task_and_200_status() throws Exception {
                mockMvc.perform(put("/tasks/"+updateTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paramTask)))
                        .andExpect(status().isOk())
                        .andExpect(content().json(objectMapper.writeValueAsString(paramTask)));
            }

        }

    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("만약 할 일 목록에 없는 할 일을 삭제한다면")
        class Context_invalid_task_id {

            @BeforeEach
            void setUp() {
                given(taskService.removeTask(NOT_FOUND_TASK_ID)).willThrow(new TaskNotFoundException());
            }

            @Test
            @DisplayName("404 상태코드와 에러 메세지를 반환합니다.")
            void it_return_404_status_and_err_message() throws Exception {
                mockMvc.perform(delete("/tasks/"+NOT_FOUND_TASK_ID))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(TASK_NOT_FOUND_ERROR_MESSAGE));
            }

        }

        @Nested
        @DisplayName("만약 할 일 목록에 있는 할 일을 삭제한다면")
        class Context_valid_task_id {
            Long deleteTaskId = NEW_TASK_ID;

            @BeforeEach
            void setUp() {

                List<Task> tasks = new ArrayList<>();

                Task newTask = new Task();
                newTask.setId(NEW_TASK_ID);
                newTask.setTitle(NEW_TASK_TITLE);
                tasks.add(newTask);

                given(taskService.removeTask(newTask.getId())).willReturn(newTask);

            }

            @Test
            @DisplayName("204 상태코드를 반환합니다.")
            void it_return_and_204_status() throws Exception {

                mockMvc.perform(delete("/tasks/"+deleteTaskId))
                        .andExpect(status().isNoContent());

            }

        }

    }

}
