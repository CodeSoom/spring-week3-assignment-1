package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest
 * Web Layer에서만 간단히 테스트가 가능.
 * 전체적인 spring context가 필요하지 않을 때 mockBean을 사용하여 웹 레이어만 테스트 가능
 */

@WebMvcTest
class TaskControllerWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;
	private ObjectMapper objectMapper;

	private final long VALID_ID = 1L;
	private final long INVALID_ID = 99L;
	private final String TASK_TITLE = "SampleTask";

	@BeforeEach
	void setUp() {
		Task task = new Task();
		task.setId(1L);
		task.setTitle(TASK_TITLE);

		when(taskService.getTask(VALID_ID))
				.thenReturn(task);
		when(taskService.getTask(INVALID_ID))
				.thenThrow(new TaskNotFoundException(INVALID_ID));
		when(taskService.getTasks())
				.thenReturn(Collections.singletonList(task));

		objectMapper = new ObjectMapper();

	}

	@Test
	void list() throws Exception {
		mockMvc.perform(get("/tasks")).andExpect(status().isOk())
				.andExpect(content().string(containsString(TASK_TITLE)));
	}


	@Test
	public void detailWithValidId() throws Exception {
		mockMvc.perform(get("/tasks/" + VALID_ID))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void detailWithInvalidId() throws Exception {
		mockMvc.perform(get("/tasks/" + INVALID_ID))
				.andExpect(status().isNotFound());
	}

	@Test
	public void createTask() throws Exception {
		Task task = new Task();
		task.setTitle("NEW Task");

		mockMvc.perform(post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isCreated())
				.andDo(print());
	}

	@Test
	public void putTask() throws Exception {
		Task task = new Task();
		task.setTitle("_UPDATE");

		mockMvc.perform(put("/tasks/" + VALID_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void updateTask() throws Exception {
		Task task = new Task();
		task.setTitle("_UPDATE");

		mockMvc.perform(patch("/tasks/" + VALID_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isOk())
				.andDo(print());
	}


}