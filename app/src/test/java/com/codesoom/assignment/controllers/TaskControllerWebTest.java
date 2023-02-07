package com.codesoom.assignment.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
class TaskControllerWebTest {

  public static final String TITLE = "Test Task Title";
  @Autowired
  MockMvc mockMvc;

  @MockBean
  TaskService taskService;

  @Autowired
  ObjectMapper objectMapper;

  private Task task;

  @BeforeEach
  void setUp() {
    task = new Task();
    task.setId(1L);
    task.setTitle(TITLE);
  }

  @Test
  void list() throws Exception {
    given(taskService.getTasks()).willReturn(Collections.singletonList(task));

    mockMvc.perform(get("/tasks"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").value(hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(equalTo(task.getId()), Long.class))
        .andExpect(jsonPath("$[0].title").value(equalTo(TITLE)));
  }

  @Test
  void create() throws Exception {
    given(taskService.createTask(any(Task.class))).willReturn(task);

    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(task)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(equalTo(task.getId()), Long.class))
        .andExpect(jsonPath("title").value(equalTo(task.getTitle())));
  }

  @Test
  void detail_withValidId() throws Exception {
    given(taskService.getTask(1L)).willReturn(task);

    mockMvc.perform(get("/tasks/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("id").value(equalTo(task.getId()), Long.class))
        .andExpect(jsonPath("title").value(equalTo(task.getTitle())));
  }

  @Test
  void detail_withInvalidId() throws Exception {
    given(taskService.getTask(1001L)).willThrow(new TaskNotFoundException(1001L));

    mockMvc.perform(get("/tasks/1001"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void put_withValidId() throws Exception {
    given(taskService.updateTask(eq(1L), any(Task.class))).willReturn(task);

    mockMvc.perform(put("/tasks/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(task)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("id").value(equalTo(task.getId()), Long.class))
        .andExpect(jsonPath("title").value(equalTo(task.getTitle())));
  }

  @Test
  void put_withInvalidId() throws Exception {
    given(taskService.updateTask(eq(1001L), any(Task.class))).willThrow(
        new TaskNotFoundException(1001L));

    mockMvc.perform(put("/tasks/1001")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(task)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void patch_withValidId() throws Exception {
    given(taskService.updateTask(eq(1L), any(Task.class))).willReturn(task);

    mockMvc.perform(patch("/tasks/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(task)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("id").value(equalTo(task.getId()), Long.class))
        .andExpect(jsonPath("title").value(equalTo(task.getTitle())));
  }

  @Test
  void patch_withInvalidId() throws Exception {
    given(taskService.updateTask(eq(1001L), any(Task.class))).willThrow(
        new TaskNotFoundException(1001L));

    mockMvc.perform(patch("/tasks/1001")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(task)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void delete_withValidId() throws Exception {
    mockMvc.perform(delete("/tasks/1"))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  void delete_withInvalidId() throws Exception {
    given(taskService.deleteTask(1001L)).willThrow(new TaskNotFoundException(1001L));

    mockMvc.perform(delete("/tasks/1001"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}