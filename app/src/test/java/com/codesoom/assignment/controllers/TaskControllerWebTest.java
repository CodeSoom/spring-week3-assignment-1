package com.codesoom.assignment.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.test.web.servlet.RequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerWebTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  private TaskService taskService;

  private RequestBuilder requestBuilder;
  private List<Task> taskList;
  private Task task;


  private final String TASK_TITLE = "Test";
  private final String CREATE_TITLE = "createTest";
  private final String MODIFY_TITLE = "modifyTest";

  private TaskController taskController;
  private Long NotFoundId = 0L;
  private Long ValidId;

  @BeforeEach
  void setUp() {
    task = new Task();
    task.setTitle(TASK_TITLE);
    taskController = new TaskController(new TaskService());
  }


  @Nested
  @DisplayName("GET /tasks 요청할 때,")
  class Context_WhenRequestGetTasks {

    @BeforeEach
    void setUp() {
      requestBuilder = get("/tasks");
    }


    @Nested
    @DisplayName("task가 없을 때")
    class Context_WithoutTask {

      @Test
      @DisplayName("200 status 코드와 빈 리스트를 return.")
      void It_Respond200AndEmptyList() throws Exception {
        given(taskService.getTasks()).willReturn(new ArrayList<>());

        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
      }
    }


    @Nested
    @DisplayName("task가 있을 때")
    class Context_WithTask {

      @BeforeEach
      void setUp() {
        taskController.create(task);
        taskList = taskController.list();
        given(taskService.getTasks()).willReturn(taskList);

      }

      @Test
      @DisplayName("200 status 코드와 task를 포함한 리스트를 return.")
      void It_Respond200AndListWithTask() throws Exception {
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(taskList)));
      }
    }
  }

  @Nested
  @DisplayName("GET tasks/{id} request는")
  class Describe_GetById {

    @Nested
    @DisplayName("invalid한 id로 reuqest를 할때")
    class Context_InvalidTaskId {

      @BeforeEach
      void setUp() {
        given(taskService.getTask(NotFoundId)).willThrow(new TaskNotFoundException(NotFoundId));
      }

      @Test
      @DisplayName("404 상태코드를 return")
      void It_Return404() throws Exception {
        mockMvc.perform(get("/tasks/" + NotFoundId))
            .andExpect(status().isNotFound());
      }
    }

    @Nested
    @DisplayName("valid한 id로 request를 할 때")
    class Context_ValidTaskId {

      @BeforeEach
      void setUp() {
        Task taskWithValidId = taskController.create(task);
        ValidId = taskWithValidId.getId();
        given(taskService.getTask(ValidId)).willReturn(taskWithValidId);

      }

      @Test
      @DisplayName("200 상태코드와 조회된 task를 return")
      void It_Return200AndTask() throws Exception {
        mockMvc.perform(get("/tasks/" + ValidId))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(TASK_TITLE)));
      }

    }

  }


}
