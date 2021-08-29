package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@DisplayName("TaskControllerWebTest 클래스")
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

    private final String[] TASK_TITLE = {"test1", "test2", "test3", "test4", "test5"};
    private final int TASKS_SIZE = TASK_TITLE.length;
    private final String TASK_UPDATE = "update";
    private final Long VALID_ID = 1L;
    private final Long INVALID_ID = 100L;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskService taskService; // 스프링에서 DI 주입


    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("등록된 일들이 1개 이상 있다면")
        class Context_exist_tasks {

            List<Task> tasks = new ArrayList<>();

            @BeforeEach
            void list_setUp() {

                for (String taskTitle : TASK_TITLE) {
                    Task task = new Task();
                    task.setTitle(taskTitle);
                    tasks.add(task);
                }

                given(taskService.getTasks()).willReturn(tasks);

            }

            @Test
            @DisplayName("task 객체들을 리턴하고 200 상태코드를 내려준다")
            void It_return_tasks_status_200() throws Exception {

                Assertions.assertThat(taskService.getTasks()).hasSize(TASKS_SIZE);
                mockMvc.perform(get("/tasks")).andExpect(status().isOk());

            }

        }

    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("리스트에 아이디가 존재하면")
        class Context_exist_id {


            @BeforeEach
            void task_setUp() {

                taskService.createTask(new Task());

                Task foundTask = new Task();
                given(taskService.getTask(VALID_ID)).willReturn(foundTask);
                
            }

            @Test
            @DisplayName("할 일을 리턴해주고 200 상태코드를 내려준다")
            void It_return_task_status_200() throws Exception {

                mockMvc.perform(get("/tasks/"+VALID_ID)).andExpect(status().isOk());

            }

        }

        @Nested
        @DisplayName("리스트에 아이디가 존재하지 않는다면")
        class Context_exist_not_id {

            @BeforeEach
            void invalid_task_setUp() {


                given(taskService.getTask(INVALID_ID)).willThrow(TaskNotFoundException.class);

                /* 이런식으로 수정을 하면 될 줄 알았는데 컴파일 에러가 나오는군요..
                given(Assertions.assertThatThrownBy(() -> {
                    taskService.getTask(INVALID_ID);
                }).isInstanceOf(TaskNotFoundException.class)).willReturn(true);
                */

            }

            @Test
            @DisplayName("TaskNotFoundException 예외를 던져주고 404 에러를 내려준다")
            void It_return_exception_status_404() throws Exception {

                mockMvc.perform(get("/tasks/"+INVALID_ID)).andExpect(status().isNotFound());

            }

        }

    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("추가할 할 일이 있다면")
        class Context_exist_task {

            Task task = new Task();

            @BeforeEach
            void task_setUp() {

                task.setTitle(TASK_TITLE[0]);
                given(taskService.createTask(task)).willReturn(task);

            }

            @Test
            @DisplayName("리스트에 할 일을 추가하고 201 상태코드를 내려준다")
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
    @DisplayName("updateTask 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("수정하고 싶은 할 일이 있다면")
        class Context_exist_update_id {

            Task updateTask = new Task();

            @BeforeEach
            void update_setUp() {

                Task task = new Task();
                task.setTitle(TASK_TITLE[0]);
                taskService.createTask(task);

                updateTask.setTitle(TASK_TITLE[0] + TASK_UPDATE);
                given(taskService.updateTask(VALID_ID, updateTask)).willReturn(updateTask);

            }

            @Test
            @DisplayName("리스트에서 아이디를 찾아 값을 수정한다")
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
    @DisplayName("deleteTask 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("삭제할 아이디가 존재하면")
        class Context_exist_delete_id {

            Task deleteTask = new Task();
            Long deleteId = VALID_ID;

            @BeforeEach
            void delete_setUp() {

                for (String taskTitle : TASK_TITLE) {
                    Task task = new Task();
                    task.setTitle(taskTitle);
                    taskService.createTask(task);
                }

                given(taskService.deleteTask(deleteId)).willReturn(deleteTask);
            }

            @Test
            @DisplayName("리스트에서 아이디를 찾아 할 일을 삭제하고 204 상태코드를 내려준다")
            void It_delete_task_status_204() throws Exception {

                mockMvc.perform(delete("/tasks/"+deleteId)).andExpect(status().isNoContent());

            }

        }

    }

}
